# SekaiShop 桌面版视觉验收记录

- 日期：2026-08-28
- 入口：`dist/out/SekaiShop/SekaiShop.exe`
- 复现：双击 exe，等待启动页切换。
- 观察：独立窗口可以创建，启动失败页可显示“重新检测并重试”按钮和日志位置提示。
- 截图：[desktop-startup-failure.png](./desktop-startup-failure.png)
- 失败原因：当前验收机上的 JDK 25 和普通 `java -jar` 均在 Tomcat `Selector.open()` 处报 `Unable to establish loopback connection`；不是 jpackage 专属问题。
- 结论：启动失败反馈通过；商品页面、登录、购物车和订单流程因本机服务无法监听 8082，记为 INCONCLUSIVE，需在 Winsock/JDK loopback 恢复或另一台 Windows 机器上复验。
