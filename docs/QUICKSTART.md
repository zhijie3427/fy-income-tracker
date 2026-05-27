# 快速启动指南

一键启动梦幻西游收益记录系统，3 分钟即可运行！

## 前置要求

确保已安装：
- JDK 11+ ✓
- Maven 3.6+ ✓
- Node.js 14+ ✓
- MySQL 5.7+ ✓

快速检查：
```bash
java -version
mvn -version
node -version
mysql --version
```

## 第 1 步：准备数据库（1 分钟）

### 创建数据库
```bash
# 登录 MySQL
mysql -u root -p

# 执行以下命令
CREATE DATABASE fy_income_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE fy_income_tracker;

# 导入建表脚本 - 复制 docs/database-mysql.sql 中的内容并执行
# 或使用命令导入
mysql -u root -p fy_income_tracker < docs/database-mysql.sql
```

## 第 2 步：启动后端（1 分钟）

### 打开终端 1 - 启动 Spring Boot
```bash
cd backend

# 编译并运行
mvn clean spring-boot:run
```

看到这条消息表示成功：
```
Started IncomeTrackerApplication in X.XXX seconds
```

后端地址：`http://localhost:8080`

## 第 3 步：启动前端（1 分钟）

### 打开终端 2 - 启动 Vue 开发服务
```bash
cd frontend

# 安装依赖（首次运行）
npm install

# 启动开发服务器
npm run dev
```

看到这条消息表示成功：
```
VITE v4.3.9  ready in xxx ms

➜  Local:   http://localhost:3000/
```

## 第 4 步：登录应用（登录）

1. 打开浏览器访问：`http://localhost:3000`
2. 点击"注册"标签页，创建账号
3. 返回"登录"标签页，使用新账号登录

## 完成！

现在可以使用应用了：
- 📊 **仪表盘** - 查看汇总数据
- 📝 **记录收益** - 新增每日收益记录
- 📈 **数据统计** - 查看图表和详细报表

---

## 常见问题快速解决

### 问题 1: 无法连接数据库
```bash
# 检查 MySQL 是否运行
mysql -u root -p
# 输入密码，看到 mysql> 提示符表示成功
```

修改 `backend/application.yml` 中的数据库配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fy_income_tracker
    username: root
    password: 你的密码
```

### 问题 2: 前端无法连接后端
- 确保后端运行在 `http://localhost:8080`
- 检查防火墙是否开放 8080 端口
- 查看前端的网络请求（浏览器 F12 -> Network）

### 问题 3: 3000/8080 端口已被占用
```bash
# 修改前端端口 - 编辑 frontend/vite.config.js
server: {
  port: 3001  // 改为其他端口
}

# 修改后端端口 - 编辑 backend/application.yml
server:
  port: 8081  # 改为其他端口
```

### 问题 4: 注册失败 - 用户名已存在
在应用中使用其他用户名注册。

### 问题 5: npm install 太慢
设置 npm 镜像源：
```bash
npm config set registry https://registry.npmmirror.com
```

---

## 项目文件结构

```
fy-income-tracker/
├── README.md                 # 项目说明
├── backend/                  # Spring Boot 后端
│   ├── pom.xml              # Maven 依赖配置
│   ├── application.yml      # 应用配置
│   └── src/
│       ├── main/java/       # Java 源代码
│       │   └── com/fy/incometracker/
│       │       ├── IncomeTrackerApplication.java  # 启动类
│       │       ├── entity/                        # 数据库实体
│       │       ├── repository/                    # 数据访问层
│       │       ├── service/                       # 业务逻辑层
│       │       ├── controller/                    # 控制器
│       │       ├── dto/                           # 数据传输对象
│       │       ├── security/                      # 安全配置
│       │       └── utils/                         # 工具类
│       └── main/resources/  # 资源文件
├── frontend/                 # Vue 3 前端
│   ├── package.json         # npm 依赖配置
│   ├── vite.config.js       # Vite 配置
│   ├── index.html           # HTML 入口
│   └── src/
│       ├── main.js          # Vue 应用入口
│       ├── App.vue          # 根组件
│       ├── components/      # 可复用组件
│       ├── views/           # 页面组件
│       │   ├── Login.vue         # 登录/注册页
│       │   ├── Dashboard.vue     # 仪表盘
│       │   ├── Record.vue        # 记录收益
│       │   └── Statistics.vue    # 数据统计
│       ├── router/          # 路由配置
│       ├── stores/          # Pinia 状态管理
│       └── utils/           # 工具函数
└── docs/                     # 文档
    ├── database-mysql.sql   # MySQL 建表脚本
    ├── LOCAL_DEPLOYMENT.md  # 本地部署指南
    ├── ALIYUN_DEPLOYMENT.md # 阿里云部署指南
    ├── API.md               # API 文档
    └── QUICKSTART.md        # 快速启动（本文件）
```

---

## 后续操作

### 测试功能
1. 创建 5 个账号（对应游戏的 5 个角色）
2. 每天记录一条收益记录
3. 查看统计数据和图表

### 自定义配置
编辑 `backend/application.yml`：
```yaml
jwt:
  secret: 修改为你的密钥  # 生产环境必须修改
  expiration: 86400000   # Token 有效期（毫秒）

app:
  cors:
    allowed-origins: http://localhost:3000  # 允许的来源
```

### 生产部署
- 本地部署：参考 `docs/LOCAL_DEPLOYMENT.md`
- 阿里云部署：参考 `docs/ALIYUN_DEPLOYMENT.md`

### 常见自定义需求
- **修改界面语言** - 编辑前端 Vue 文件
- **调整数据字段** - 修改数据库表和后端实体
- **添加新功能** - 参考 API 文档新增接口
- **美化界面** - 使用 Element Plus 组件库

---

## 获取帮助

查看详细文档：
- 📚 **API 文档** - `docs/API.md`
- 🚀 **部署指南** - `docs/LOCAL_DEPLOYMENT.md`
- ☁️ **阿里云部署** - `docs/ALIYUN_DEPLOYMENT.md`

---

## 许可证
MIT

祝搬砖愉快！💰
