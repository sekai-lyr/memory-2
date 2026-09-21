# memory-2 · Sekai 商城（eBusiness 电商练习项目）

一个基于 **Spring Boot + MyBatis + Thymeleaf** 的完整电商练习项目，使用 `com.youkeda.application.ebusiness` 包结构，采用 control / service / dao / dataobject / model 分层设计。

## 功能

- **用户**：注册（注册即登录）、登录（Session 会话）、退出登录
- **市场**：在售商品浏览、搜索 / 分类筛选 / 排序、网格与列表双视图、商品详情页（图片画廊、数量选择）
- **买卖闭环**：
  - 购物车：加购、数量调整、单选/全选、购物车结算
  - 订单：下单（收货信息）→ 模拟支付（扣减库存）→ 卖家发货 → 买家确认收货
  - 取消订单：待支付直接取消；已支付取消自动回补库存
  - 买家订单页（状态 Tab + 操作按钮）、卖家订单管理页（发货、销售统计）
- **商品管理（按用户隔离）**：我的商品页（发布 / 编辑 / 上架 / 下架 / 删除），市场只展示在售商品
- **图片**：本地上传（拖拽 / 选择文件 / URL），存于 `uploads/` 并映射 `/uploads/**`；种子商品为 **Booth（pixiv 旗下）二次元手办/周边官方商品图**（`uploads/anime/` 33 个 + `uploads/anime2/` 1000 个）

## 商品图说明

**1000 个二次元周边商品**（Booth 商城 booth.pm 官方商品图，`uploads/anime2/b{1..1000}-{main,detail}.jpg`）：覆盖初音未来、V家、东方Project、FGO、原神、崩坏星穹铁道、明日方舟、碧蓝航线、赛马娘、孤独摇滚、咒术回战、EVA、鬼灭之刃、间谍过家家、Lycoris Recoil 等 80+ 动漫 IP，品类包括亚克力立牌、PVC 手办、黏土人、缶バッジ徽章、钥匙扣挂件、毛绒玩偶、色纸画集、CD、同人志、T恤、抱枕痛包等（按名称关键词自动分类到二次元分类体系）。数据库 `images`/`detail` 字段即为 `/uploads/anime2/...` 路径，新上架商品可通过发布页上传自己的商品图。

## 项目结构

- `control/`：接口入口（页面路由 + JSON API）
- `config/`：上传目录映射、登录会话工具
- `service/`：业务逻辑
- `dao/`：数据访问层
- `dataobject/`：数据库实体
- `model/`：前后端传输对象
- `data-build/`：数据库初始化与升级脚本
- `uploads/seed/`：种子商品 SVG 占位图

## 技术栈

- Java 21 + Spring Boot 4 + Spring MVC
- MyBatis（XML Mapper）
- Thymeleaf + 原生 JS（共享 `common.css` / `common.js` 设计系统）
- Maven

## 数据库

```powershell
# 1. 初始化（首次）
mysql -u root -p sekai_friend < data-build\index.sql
# 2. 升级 v2（购物车/订单表 + 种子图片与 user_id 修复）
mysql -u root -p sekai_friend < data-build\upgrade-v2.sql
# 3. 升级 v3（二次元化：手办周边商品 + 二次元分类，图片在 uploads/anime/）
mysql -u root -p sekai_friend < data-build\upgrade-v3-anime.sql
# 4. 升级 v4（批量导入 1000 个二次元周边商品，图片在 uploads/anime2/）
#    注意：需 utf8mb4 支持 emoji 商品名，并建议用 mysql 客户端管道导入：
mysql -u root -p --default-character-set=utf8mb4 sekai_friend < data-build\upgrade-v4-mass.sql
mysql -u root -p --default-character-set=utf8mb4 sekai_friend < data-build\reclassify.sql
```

连接配置见 `src/main/resources/application.properties`（默认 8082 端口，`sekai_friend` 库）。

## 运行

```powershell
mvn package -DskipTests
java -jar target\ebusiness-0.0.1-SNAPSHOT.jar
```

访问 http://localhost:8082/product/list ，种子账号：`zhangsan / 123456`、`lisi / 123456` 等。

## Windows 桌面版

项目也提供不依赖外部浏览器的绿色版桌面应用：`SekaiShop.exe` 内嵌 JavaFX WebView，
并随窗口启动/退出 Spring Boot 服务。绿色版自带 Java 运行时，目标机器无需安装 Java，
但仍需本机运行 MySQL 8，并提前初始化 `sekai_friend` 数据库。

构建桌面版（需要 JDK 21+、Maven、OpenJFX SDK）：

```powershell
powershell -ExecutionPolicy Bypass -File tools\build-exe.ps1
```

完整产物位于 `dist\out\SekaiShop\`，压缩包为
`dist\SekaiShop-v1.0.0-win-x64.zip`。双击 `SekaiShop.exe` 后在窗口内完成浏览、登录、
购物和订单操作；启动失败时查看 `app\sekai.log`。详细系统要求和常见问题见
`tools\README-exe.md`（打包后为绿色版根目录的《使用说明》）。
