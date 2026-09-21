package com.youkeda.application.ebusiness.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;

/**
 * exe 版专用：应用启动完成后自动打开浏览器。
 * 仅当系统属性 app.open-browser=true 时生效（由打包命令通过 -D 注入），
 * 正常开发/部署运行不受影响。
 */
@Component
public class BrowserOpener {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrowserOpener.class);

    @Value("${app.open-browser:false}")
    private boolean openBrowser;

    @Value("${server.port:8082}")
    private int port;

    @EventListener(ApplicationReadyEvent.class)
    public void openHomePage() {
        if (!openBrowser) {
            return;
        }
        String url = "http://localhost:" + port + "/product/list";
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI.create(url));
                LOGGER.info("商城已启动，已在浏览器打开：{}", url);
            } else {
                LOGGER.warn("当前环境不支持自动打开浏览器，请手动访问：{}", url);
            }
        } catch (Exception e) {
            LOGGER.warn("自动打开浏览器失败，请手动访问 {}（原因：{}）", url, e.getMessage());
        }
    }
}
