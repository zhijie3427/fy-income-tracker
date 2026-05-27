# 阿里云部署指南

## 架构设计

```
┌─────────────────┐
│   域名 + CDN     │
└────────┬────────┘
         │
    ┌────▼────┐
    │ Nginx   │ (端口 80/443)
    └────┬────┘
         │
    ┌────▼──────────────┬────────────┐
    │                   │            │
┌───▼────┐      ┌──────▼──┐    ┌────▼─────┐
│ Frontend│      │ Backend  │    │ Caching  │
│(Vue3)  │      │(Spring   │    │ (Redis)  │
│        │      │ Boot)    │    │          │
└────────┘      └──┬───┬──┘    └──────────┘
                   │   │
              ┌────▼───▼─┐
              │ Database │
              │ (RDS)    │
              └──────────┘
```

## 前置条件

1. 已注册阿里云账号
2. 已开通 ECS、RDS、SLB 等服务
3. 已备案域名（中国内地需要）

## 第一步：创建 ECS 实例

### 1. 进入阿里云控制台
访问：https://console.aliyun.com/

### 2. 创建 ECS 实例
- 地域：选择合适地域（如华东1）
- 实例类型：1 核 2GB 或以上
- 镜像：Ubuntu 20.04 LTS
- 网络：选择 VPC 和安全组

### 3. 配置安全组规则
- 入站规则：
  - 3306（MySQL）- 仅内部
  - 8080（后端） - 限制 IP
  - 80/443（HTTP/HTTPS） - 允许所有
  - 22（SSH） - 限制 IP

## 第二步：部署后端

### 1. 连接到 ECS
```bash
ssh -i your_key.pem ubuntu@your_eip
```

### 2. 安装依赖
```bash
sudo apt update
sudo apt install -y openjdk-11-jdk maven
java -version
mvn -version
```

### 3. 部署应用
```bash
# 上传项目
scp -i your_key.pem -r fy-income-tracker/backend ubuntu@your_eip:/home/ubuntu/

# 连接后编译运行
cd ~/fy-income-tracker/backend
mvn clean package -DskipTests

# 使用 systemd 管理服务
sudo nano /etc/systemd/system/fy-income-tracker.service
```

写入以下内容：
```ini
[Unit]
Description=FY Income Tracker Backend
After=network.target

[Service]
Type=simple
User=ubuntu
WorkingDirectory=/home/ubuntu/fy-income-tracker/backend
ExecStart=/usr/lib/jvm/java-11-openjdk-amd64/bin/java -jar target/income-tracker-1.0.0.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

启动服务：
```bash
sudo systemctl daemon-reload
sudo systemctl enable fy-income-tracker
sudo systemctl start fy-income-tracker
```

### 4. 查看日志
```bash
sudo journalctl -u fy-income-tracker -f
```

## 第三步：部署前端

### 1. 安装 Node.js
```bash
sudo apt install -y nodejs npm
node --version
npm --version
```

### 2. 部署前端
```bash
cd ~/fy-income-tracker/frontend
npm install --production
npm run build

# 将 dist 文件夹复制到 Nginx
sudo cp -r dist /var/www/fy-income-tracker
```

## 第四步：配置数据库 (RDS)

### 1. 创建 RDS MySQL 实例
在阿里云控制台：
- 选择 RDS MySQL
- 实例类型：云数据库 MySQL 5.7+
- 配置存储和内存

### 2. 创建数据库
```bash
mysql -h your_rds_host -u admin -p

CREATE DATABASE fy_income_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE fy_income_tracker;
-- 导入建表脚本
SOURCE docs/database-mysql.sql;
```

### 3. 修改后端配置
编辑 `application-prod.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://your_rds_host:3306/fy_income_tracker?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
    username: admin
    password: your_password
  jpa:
    hibernate:
      ddl-auto: validate
```

## 第五步：配置 Nginx

### 1. 安装 Nginx
```bash
sudo apt install -y nginx
```

### 2. 配置
编辑 `/etc/nginx/sites-available/default`：

```nginx
server {
    listen 80;
    listen [::]:80;

    server_name your_domain.com www.your_domain.com;

    # 重定向到 HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    listen [::]:443 ssl http2;

    server_name your_domain.com www.your_domain.com;

    # SSL 证书配置
    ssl_certificate /etc/ssl/certs/your_cert.crt;
    ssl_certificate_key /etc/ssl/private/your_key.key;

    # 前端
    location / {
        root /var/www/fy-income-tracker;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # 后端代理
    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # 性能优化
    gzip on;
    gzip_types text/plain text/css application/json application/javascript;
    gzip_min_length 1000;
}
```

### 3. 启动 Nginx
```bash
sudo nginx -t
sudo systemctl restart nginx
```

## 第六步：配置 HTTPS

### 1. 申请免费 SSL 证书（可选）
使用 Let's Encrypt 和 Certbot：
```bash
sudo apt install -y certbot python3-certbot-nginx
sudo certbot certonly -d your_domain.com --webroot -w /var/www/fy-income-tracker
```

## 第七步：监控和维护

### 1. 设置日志收集
在应用配置中启用日志输出到文件

### 2. 定期备份
```bash
# 定期备份数据库
mysqldump -h your_rds_host -u admin -p fy_income_tracker > backup_$(date +%Y%m%d_%H%M%S).sql
```

### 3. 性能监控
- 使用阿里云云监控检查 ECS 性能
- 监控 RDS 数据库性能

## 自动化脚本

创建 `deploy.sh` 自动部署脚本：

```bash
#!/bin/bash

# 拉取最新代码
cd /home/ubuntu/fy-income-tracker
git pull origin main

# 重新编译后端
cd backend
mvn clean package -DskipTests

# 重启服务
sudo systemctl restart fy-income-tracker

# 重新构建前端
cd ../frontend
npm install
npm run build
sudo cp -r dist /var/www/fy-income-tracker

# 重启 Nginx
sudo systemctl restart nginx

echo "部署完成！"
```

授予执行权限：
```bash
chmod +x deploy.sh
```

## 常见问题

### Q: 如何更新应用代码？
A: 使用上面的 deploy.sh 脚本，或手动执行更新步骤。

### Q: 如何增加服务器性能？
A: 在阿里云控制台升级 ECS 实例配置或使用 SLB 负载均衡。

### Q: 如何备份数据？
A: 使用 mysqldump 定期备份，或启用 RDS 自动备份。

## 下一步

- 配置 CDN 加速静态资源
- 添加监控告警
- 实施自动化运维流程
- 优化应用性能

访问您的网站：`https://your_domain.com`
