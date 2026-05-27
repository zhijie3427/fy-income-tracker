# 本地部署指南

## 前置要求

### 必需软件
- **JDK 11+** - Java 开发环境
- **Maven 3.6+** - Java 项目管理工具
- **Node.js 14+** - JavaScript 运行环境
- **MySQL 5.7+** 或 **PostgreSQL 12+** - 数据库

### 安装步骤

#### 1. 安装 JDK 11+
下载并安装：https://www.oracle.com/java/technologies/downloads/

验证安装：
```bash
java -version
javac -version
```

#### 2. 安装 Maven
下载并安装：https://maven.apache.org/download.cgi

验证安装：
```bash
mvn -version
```

#### 3. 安装 Node.js
下载并安装：https://nodejs.org/

验证安装：
```bash
node --version
npm --version
```

#### 4. 安装 MySQL
下载并安装：https://dev.mysql.com/downloads/mysql/

创建数据库：
```bash
mysql -u root -p
```

```sql
CREATE DATABASE fy_income_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE fy_income_tracker;
-- 导入 docs/database-mysql.sql 中的 SQL 脚本
```

## 后端部署

### 1. 进入后端目录
```bash
cd fy-income-tracker/backend
```

### 2. 配置数据库
编辑 `application.yml` 文件：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fy_income_tracker?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
    username: root
    password: your_password
```

### 3. 构建项目
```bash
mvn clean install
```

### 4. 运行项目
```bash
mvn spring-boot:run
```

或者先编译，再运行：
```bash
mvn clean package
java -jar target/income-tracker-1.0.0.jar
```

后端将在 `http://localhost:8080` 运行

## 前端部署

### 1. 进入前端目录
```bash
cd fy-income-tracker/frontend
```

### 2. 安装依赖
```bash
npm install
```

### 3. 开发模式运行
```bash
npm run dev
```

前端将在 `http://localhost:3000` 运行

### 4. 生产构建
```bash
npm run build
```

编译后的文件将在 `dist/` 目录中

## 完整启动流程

### 终端 1 - 启动后端
```bash
cd backend
mvn spring-boot:run
```

### 终端 2 - 启动前端
```bash
cd frontend
npm install
npm run dev
```

然后访问 `http://localhost:3000`

## 常见问题

### Q: 连接数据库失败？
A: 检查 MySQL 是否运行，用户名密码是否正确，数据库是否创建。

### Q: 前端无法连接后端？
A: 检查后端服务是否运行在 8080 端口，以及 vite.config.js 中的代理配置。

### Q: 3000 端口已被占用？
A: 在 vite.config.js 中修改 server.port 为其他端口。

### Q: Maven 下载依赖很慢？
A: 配置 Maven 镜像源，编辑 `~/.m2/settings.xml`

## 项目结构说明

```
fy-income-tracker/
├── backend/                    # Spring Boot 后端
│   ├── src/
│   │   ├── main/java/         # Java 源代码
│   │   ├── main/resources/    # 资源文件
│   │   └── test/              # 测试文件
│   ├── pom.xml                # Maven 配置
│   └── application.yml        # 应用配置
├── frontend/                   # Vue 3 前端
│   ├── src/
│   │   ├── components/        # 组件
│   │   ├── views/             # 页面
│   │   ├── router/            # 路由配置
│   │   ├── stores/            # 状态管理
│   │   └── utils/             # 工具函数
│   ├── package.json           # npm 配置
│   └── vite.config.js         # Vite 配置
└── docs/                       # 文档
    ├── database-mysql.sql     # MySQL 建表脚本
    └── LOCAL_DEPLOYMENT.md    # 本地部署指南
```

## 下一步

- 修改 JWT 密钥：在 application.yml 中修改 `jwt.secret`
- 配置跨域：修改 application.yml 中的 `app.cors.allowed-origins`
- 自定义业务逻辑：根据需求修改后端服务类和控制器
- 美化前端界面：定制 Element Plus 主题和样式
