# FY Income Tracker Backend

Spring Boot 后端服务，提供收益记录、账号管理和物品维护接口。

## 运行前准备

1. 安装 MySQL。
2. 创建数据库：
   ```sql
   CREATE DATABASE fy_income_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
3. 修改 `backend/src/main/resources/application.yml` 中的数据库用户名/密码。

## 启动

```bash
cd backend
mvn spring-boot:run
```

## API 入口

- `GET /items` 物品列表
- `POST /items` 新建物品
- `PUT /items/{id}` 更新物品
- `DELETE /items/{id}` 删除物品
- `GET /accounts` 账号列表
- `GET /income-records` 收益记录列表
- `POST /income-records` 保存收益记录

前端可通过 `http://localhost:3000` 访问，后端默认监听 `http://localhost:8080`。