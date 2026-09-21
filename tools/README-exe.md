# Sekai 商城 · 桌面版（SekaiShop.exe）使用说明

一个不依赖浏览器的 Windows 桌面应用：双击 exe，商城会以独立窗口打开，
内置的 Web 服务（Spring Boot）随应用一起启动/退出，无需手动开浏览器、无需安装 Java。

## 系统要求

- Windows 10 / 11（64 位）
- 本机已安装 **MySQL 8** 并运行在 `localhost:3306`
  - 账号 `root`，密码 `123456520baba`（与项目 `application.properties` 一致）
  - 已初始化 `sekai_friend` 数据库（含 1000 个种子商品，导入脚本见项目 `data-build/` 目录）

## 使用方法

1. 双击 `SekaiShop.exe`；
2. 等待片刻（首次启动约 5~10 秒），应用窗口自动打开商城首页；
3. 在窗口内完成注册 / 登录 / 购物 / 下单等全部操作；
4. 关闭窗口即停止服务并退出程序。

种子账号：`zhangsan / 123456`、`lisi / 123456` 等。

## 目录说明

```
SekaiShop/
├── SekaiShop.exe        # 桌面应用入口（双击运行）
├── app/
│   ├── ebusiness-*.jar  # 商城后端（内置 Spring Boot）
│   ├── fx-launcher.jar  # 桌面启动器（内嵌窗口引擎）
│   ├── uploads/         # 商品图片（含 1000 个种子商品图）
│   └── sekai.log        # 运行日志（启动失败时查看）
└── runtime/             # 内置 Java 运行时（无需另装 Java）
```

## 常见问题

| 现象 | 原因 / 处理 |
|---|---|
| 窗口提示"启动失败" | ① 确认 MySQL 已启动；② 确认 `sekai_friend` 库已初始化；③ 释放 8082 端口；④ 点击窗口内“重新检测并重试”；⑤ 查看 `app/sekai.log` |
| 端口 8082 被占用 | 关闭占用 8082 的程序后重试 |
| 关闭窗口后进程仍在 | 直接再次双击运行即可；极端情况可在任务管理器结束 `SekaiShop.exe` |
| 杀毒软件拦截 | 首次运行若被拦截，选择"允许"即可（程序未签名，属正常提示） |

## 重新打包

项目目录下执行：

```powershell
powershell -ExecutionPolicy Bypass -File tools\build-exe.ps1
```

要求：JDK 21+（含 jpackage）、Maven、OpenJFX 25 SDK（脚本头部可改路径）。
