# 🛒 Sekai eBusiness · 电商练习项目

> **Spring Boot + MyBatis 分层电商后端**
> A clean, layered e-commerce backend built with Spring Boot + MyBatis

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)
[![MyBatis](https://img.shields.io/badge/MyBatis-3-lightgrey)](https://mybatis.org/)
[![Maven](https://img.shields.io/badge/Maven-Wrapper-blue)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

基于 **Spring Boot + MyBatis** 的电商业务练习项目，采用 `com.youkeda.application.ebusiness` 包结构，分层清晰，适合作为电商后端入门与进阶参考。

An e-commerce practice project with **clear layered architecture** — a great reference for learning backend development.

## 📐 Layered Architecture / 分层架构

```text
control ──→ service ──→ dao ──→ MySQL
    ↑           ↑
    └── model ───┘ (DTO)
```

- `control/`：接口入口 API controllers
- `service/`：业务逻辑 Business logic
- `dao/`：数据访问层 Data access
- `dataobject/`：数据库实体 DB entities
- `model/`：前后端传输对象 DTOs
- `data-build/`：数据库初始化脚本 DB init scripts

## 🛠️ Tech Stack / 技术栈

- Java 17 + Spring Boot 3.x
- MyBatis
- Maven（含 `mvnw` 包装器）

## ▶️ Quick Start / 快速开始

```powershell
# 1. 初始化数据库（脚本在 data-build/）
# 2. 配置 src/main/resources/application.properties 中的数据库账号
mvn spring-boot:run
```

数据库密码通过环境变量注入（`DB_PASSWORD`），避免硬编码。

## 📝 Notes / 说明

本项目为学习/练习性质项目，适合作为电商后端入门参考。

## 📄 License

[MIT](LICENSE) © 2026 [sekai-lyr](https://github.com/sekai-lyr)

---

**⭐ If this project helped you, star it! 如果这个项目对你有帮助，欢迎 Star！**
