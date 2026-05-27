# API 文档

## 基础信息
- 基础 URL: `/api`
- 认证: JWT Token (Authorization: Bearer {token})
- 响应格式: JSON

## 认证相关接口

### 登录
```
POST /auth/login
Content-Type: application/json

{
  "username": "string",
  "password": "string"
}

Response:
{
  "userId": 1,
  "username": "string",
  "email": "string",
  "token": "jwt_token"
}
```

### 注册
```
POST /auth/register
Content-Type: application/json

{
  "username": "string",
  "password": "string",
  "email": "string",
  "phone": "string" (可选)
}

Response:
{
  "id": 1,
  "username": "string",
  "email": "string",
  "phone": "string",
  "enabled": true,
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00"
}
```

### 获取用户信息
```
GET /auth/user/{userId}

Response:
{
  "id": 1,
  "username": "string",
  "email": "string",
  "phone": "string",
  "enabled": true,
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00"
}
```

## 账号相关接口

### 创建账号
```
POST /accounts
Content-Type: application/json

{
  "userId": 1,
  "accountName": "角色名",
  "accountIndex": 1,
  "status": "正常",
  "notes": "备注"
}

Response: 同上
```

### 获取用户的所有账号
```
GET /accounts/user/{userId}

Response:
[
  {
    "id": 1,
    "userId": 1,
    "accountName": "角色名",
    "accountIndex": 1,
    "status": "正常",
    "notes": "备注",
    "createdAt": "2024-01-01T00:00:00",
    "updatedAt": "2024-01-01T00:00:00"
  }
]
```

### 获取单个账号
```
GET /accounts/{accountId}

Response: 同上
```

### 更新账号
```
PUT /accounts/{accountId}
Content-Type: application/json

{
  "accountName": "新角色名",
  "status": "休息",
  "notes": "新备注"
}

Response: 更新后的账号信息
```

### 删除账号
```
DELETE /accounts/{accountId}

Response: 204 No Content
```

## 收益记录相关接口

### 创建收益记录
```
POST /income-records
Content-Type: application/json

{
  "userId": 1,
  "accountId": 1,
  "recordDate": "2024-01-01",
  "totalIncome": 1000.00,
  "dungeonIncome": 500.00,
  "stallIncome": 300.00,
  "taskIncome": 200.00,
  "otherIncome": 0.00,
  "itemCost": 100.00,
  "goldCost": 50.00,
  "otherCost": 0.00,
  "timeSpent": 120,
  "notes": "记录备注"
}

Response:
{
  "id": 1,
  "userId": 1,
  "accountId": 1,
  "recordDate": "2024-01-01",
  "totalIncome": 1000.00,
  "dungeonIncome": 500.00,
  "stallIncome": 300.00,
  "taskIncome": 200.00,
  "otherIncome": 0.00,
  "itemCost": 100.00,
  "goldCost": 50.00,
  "otherCost": 0.00,
  "netIncome": 850.00,
  "timeSpent": 120,
  "notes": "记录备注",
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00"
}
```

### 获取单条记录
```
GET /income-records/{recordId}

Response: 同上
```

### 更新记录
```
PUT /income-records/{recordId}
Content-Type: application/json

{
  "totalIncome": 1100.00,
  "netIncome": 950.00,
  ...
}

Response: 更新后的记录
```

### 删除记录
```
DELETE /income-records/{recordId}

Response: 204 No Content
```

### 获取用户指定时间范围内的记录
```
GET /income-records/user/{userId}?startDate=2024-01-01&endDate=2024-01-31

Response:
[
  { 记录1 },
  { 记录2 },
  ...
]
```

### 获取账号指定时间范围内的记录
```
GET /income-records/account/{accountId}?startDate=2024-01-01&endDate=2024-01-31

Response:
[
  { 记录1 },
  { 记录2 },
  ...
]
```

### 获取用户特定日期的记录
```
GET /income-records/user/{userId}/date/2024-01-01

Response:
[
  { 记录1 },
  { 记录2 },
  ...
]
```

### 获取用户统计数据
```
GET /income-records/statistics/user/{userId}?startDate=2024-01-01&endDate=2024-01-31

Response:
{
  "totalIncome": 10000.00,
  "totalNetIncome": 8500.00,
  "startDate": "2024-01-01",
  "endDate": "2024-01-31"
}
```

## 错误处理

所有错误响应格式：
```json
{
  "error": "错误描述",
  "message": "详细信息",
  "timestamp": "2024-01-01T00:00:00"
}
```

常见错误码：
- 401: 未授权（需要登录或 Token 过期）
- 403: 禁止访问（无权限）
- 404: 资源不存在
- 400: 请求参数错误
- 500: 服务器错误

## 使用示例

### JavaScript/Axios
```javascript
import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000
})

// 添加 token
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 登录
const login = async () => {
  const response = await api.post('/auth/login', {
    username: 'test',
    password: 'password'
  })
  localStorage.setItem('token', response.data.token)
}

// 创建记录
const createRecord = async (data) => {
  return await api.post('/income-records', data)
}
```

### cURL
```bash
# 登录
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"password"}'

# 创建记录
curl -X POST http://localhost:8080/api/income-records \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{...}'
```
