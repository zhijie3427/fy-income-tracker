# 梦幻西游五开搬砖收益记录系统

一个用于记录和统计梦幻西游五开搬砖日常收益的 Web 应用程序。

## 功能特性

- 📊 **多账号管理** - 支持记录 5 个账号的数据
- 💰 **收益记录** - 详细记录每日收益和投入成本
- 📈 **数据统计** - 提供图表、表格等多维度统计分析
- 📥 **数据导出** - 支持导出为 Excel 等格式
- 👥 **多用户管理** - 支持多个玩家使用同一系统
- 🔐 **数据安全** - 用户认证和权限管理

## 技术栈

### 后端
- Java 11+
- Spring Boot 2.7+
- Spring Data JPA
- PostgreSQL / MySQL

### 前端
- Vue 3
- Element Plus
- ECharts
- Axios

## 快速开始

### 前置要求
- JDK 11+
- Node.js 14+
- PostgreSQL 或 MySQL

### 后端启动
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### 前端启动
```bash
cd frontend
npm install
npm run dev
```

## 项目结构

```
fy-income-tracker/
├── backend/              # Spring Boot 后端
├── frontend/             # Vue 3 前端
├── docs/                # 文档和数据库脚本
└── README.md
```

## 部署

- 本地部署：参考 docs/LOCAL_DEPLOYMENT.md
- 阿里云部署：参考 docs/ALIYUN_DEPLOYMENT.md

## 许可证

MIT
