package com.youkeda.application.ebusiness;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import netscape.javascript.JSObject;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 桌面版启动器（exe 入口）。
 *
 * 与 spring-boot fat jar 一起打包：本类位于独立的 fx-launcher.jar（普通 classpath 可加载），
 * 通过反射调用 fat jar 内的 JarLauncher 在后台线程启动 Spring Boot 服务，
 * 再用内嵌 WebView 渲染商城页面 —— 不依赖、不打开外部浏览器。
 */
public class FxLauncher {

    private static final String HOME_PATH = "/product/list";
    private static final String MYSQL_HOST = "127.0.0.1";
    private static final int MYSQL_PORT = 3306;
    private static final int DEFAULT_SERVER_PORT = 8082;
    private static final int PORT_CHECK_TIMEOUT_MS = 700;

    public static void main(String[] args) {
        // 1. 上传目录 / 日志目录固定在程序所在目录，保证绿色版可以随意拷贝移动
        try {
            Path base = Paths.get(FxLauncher.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI()).getParent();
            System.setProperty("app.upload-dir", base.resolve("uploads").toAbsolutePath().toString());
            System.setProperty("logging.file.name", base.resolve("sekai.log").toAbsolutePath().toString());
        } catch (Exception ignored) {
            // 保留默认配置
        }

        // 打开桌面窗口（main 类不继承 Application，避免 classpath 模式下的
        //    “JavaFX runtime components are missing” 检查误报）
        Application.launch(FxApp.class, args);
    }

    public static class FxApp extends Application {

        private WebView webView;
        private Label splash;
        private volatile boolean loaded = false;
        private volatile boolean startupAttemptInProgress = false;
        private final RetryHandler retryHandler = new RetryHandler();

        @Override
        public void start(Stage stage) {
            webView = new WebView();
            webView.getEngine().setJavaScriptEnabled(true);
            webView.setVisible(false);

            splash = new Label("Sekai 商城正在启动，请稍候…");
            splash.setFont(new Font(20));
            StackPane root = new StackPane(splash);
            root.getChildren().add(webView);

            webView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    JSObject window = (JSObject) webView.getEngine().executeScript("window");
                    window.setMember("sekaiLauncher", retryHandler);
                }
            });

            stage.setScene(new Scene(root, 1280, 800));
            stage.setTitle("Sekai 商城");
            stage.setMinWidth(960);
            stage.setMinHeight(600);
            stage.setOnCloseRequest(e -> System.exit(0));
            stage.show();

            // 后台预检本地依赖，通过后再启动 Spring Boot 服务
            startAttempt();
        }

        private void startAttempt() {
            if (startupAttemptInProgress) {
                return;
            }
            startupAttemptInProgress = true;
            Thread checker = new Thread(this::checkAndStartServer, "sekai-startup-check");
            checker.setDaemon(true);
            checker.start();
        }

        private void checkAndStartServer() {
            int port = serverPort();
            String url = "http://localhost:" + port + HOME_PATH;

            if (!isTcpPortOpen(MYSQL_HOST, MYSQL_PORT, PORT_CHECK_TIMEOUT_MS)) {
                showError("未检测到 MySQL（127.0.0.1:3306）正在运行。请启动 MySQL 后点击“重新检测并重试”。");
                return;
            }
            if (isTcpPortOpen("127.0.0.1", port, PORT_CHECK_TIMEOUT_MS)) {
                showError("端口 " + port + " 已被其他程序占用。请释放该端口后点击“重新检测并重试”。");
                return;
            }

            String[] args = getParameters().getRaw().toArray(new String[0]);
            Thread boot = new Thread(() -> {
                try {
                    Class<?> launcher = Class.forName("org.springframework.boot.loader.launch.JarLauncher");
                    launcher.getMethod("main", String[].class).invoke(null, (Object) args);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }, "sekai-spring-boot");
            boot.setDaemon(true);
            boot.start();

            waitForServer(url, boot);
        }

        private void waitForServer(String url, Thread boot) {
            int tries = 0;
            while (tries++ < 240) { // 最多约 2 分钟
                if (isUp(url)) {
                    final String u = url;
                    Platform.runLater(() -> {
                        if (!loaded) {
                            loaded = true;
                            splash.setVisible(false);
                            webView.setVisible(true);
                            webView.getEngine().load(u);
                        }
                    });
                    startupAttemptInProgress = false;
                    return;
                }
                if (!boot.isAlive()) {
                    showError("Spring Boot 服务启动失败，请查看程序目录下的 sekai.log 后重试。");
                    return;
                }
                sleep(500);
            }
            showError("等待本地服务超时，请查看程序目录下的 sekai.log 后重试。");
        }

        private boolean isUp(String url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setConnectTimeout(2000);
                connection.setReadTimeout(3000);
                connection.setRequestMethod("GET");
                return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
            } catch (Exception e) {
                return false;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        private void showError(String message) {
            startupAttemptInProgress = false;
            Platform.runLater(() -> {
                if (!loaded) {
                    loaded = true;
                    splash.setVisible(false);
                    webView.setVisible(true);
                    webView.getEngine().loadContent(errorPage(message));
                }
            });
        }

        private static int serverPort() {
            try {
                return Integer.parseInt(System.getProperty("server.port", String.valueOf(DEFAULT_SERVER_PORT)));
            } catch (NumberFormatException e) {
                return DEFAULT_SERVER_PORT;
            }
        }

        private static boolean isTcpPortOpen(String host, int port, int timeoutMillis) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), timeoutMillis);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        private static String errorPage(String message) {
            return "<html><head><meta charset='utf-8'></head><body style='font-family:Microsoft YaHei;"
                    + "background:#f5f6fa;text-align:center;padding-top:80px;color:#333'>"
                    + "<h2 style='color:#d33'>Sekai 商城启动失败</h2>"
                    + "<p style='margin:24px auto;max-width:680px'>" + message + "</p>"
                    + "<p>数据库名称：sekai_friend；初始化方式见随附《使用说明》</p>"
                    + "<button onclick='window.sekaiLauncher.retry()' style='padding:10px 24px;"
                    + "border:0;border-radius:6px;background:#ff5000;color:#fff;font-size:15px;cursor:pointer'>"
                    + "重新检测并重试</button>"
                    + "<p>详细日志请查看程序目录下的 sekai.log</p>"
                    + "</body></html>";
        }

        public final class RetryHandler {
            public void retry() {
                Platform.runLater(() -> {
                    if (!startupAttemptInProgress) {
                        loaded = false;
                        webView.setVisible(false);
                        splash.setText("正在重新检测，请稍候…");
                        splash.setVisible(true);
                        startAttempt();
                    }
                });
            }
        }

        private static void sleep(long ms) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
