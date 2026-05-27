# 功能说明与自定义指南

## 系统功能特性

### ✅ 已实现的功能

#### 1. 用户管理
- [x] 用户注册
- [x] 用户登录
- [x] 用户信息查询
- [x] JWT 令牌认证

#### 2. 账号管理
- [x] 创建账号（支持 5 个账号）
- [x] 修改账号信息
- [x] 删除账号
- [x] 查看账号列表
- [x] 账号状态管理（正常/休息/暂停）

#### 3. 收益记录
- [x] 新增收益记录
- [x] 编辑记录
- [x] 删除记录
- [x] 多维度收益分类：
  - 副本收益
  - 摆摊收益
  - 任务收益
  - 其他收益
- [x] 成本记录：
  - 物品成本
  - 金币成本
  - 其他成本
- [x] 自动计算净收益
- [x] 记录花费时间
- [x] 添加备注

#### 4. 数据统计
- [x] 按日期范围统计
- [x] 按账号统计
- [x] 总收益统计
- [x] 净收益统计
- [x] 收益趋势图表
- [x] 详细记录表格
- [x] Excel 导出（开发中）

#### 5. 仪表盘
- [x] 今日净收益展示
- [x] 本周净收益展示
- [x] 本月净收益展示
- [x] 账号数量展示
- [x] 最近记录列表

---

## 如何进行自定义

### 1. 修改数据字段

#### 场景：需要添加新的收益类型

**步骤 1：修改数据库表**
编辑 `docs/database-mysql.sql`，在 `income_records` 表中添加新列：

```sql
ALTER TABLE income_records ADD COLUMN bountiful_task_income DECIMAL(15, 2);
```

**步骤 2：修改后端实体**
编辑 `backend/src/main/java/com/fy/incometracker/entity/IncomeRecord.java`：

```java
@Column(precision = 15, scale = 2)
private BigDecimal bountifulTaskIncome;  // 悬赏任务收益
```

**步骤 3：修改前端表单**
编辑 `frontend/src/views/Record.vue`，添加输入框：

```vue
<el-col :xs="24" :sm="12" :md="6">
  <el-form-item label="悬赏任务收益">
    <el-input-number v-model="form.bountifulTaskIncome" :precision="2" />
  </el-form-item>
</el-col>
```

### 2. 修改界面样式

#### 场景：更改主题颜色

编辑 `frontend/src/main.js`，配置 Element Plus 主题：

```javascript
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 自定义 CSS 变量
import './styles/theme.css'

app.use(ElementPlus)
```

创建 `frontend/src/styles/theme.css`：

```css
:root {
  --el-color-primary: #409eff;  /* 修改主色调 */
  --el-color-success: #67c23a;
  --el-color-warning: #e6a23c;
  --el-color-danger: #f56c6c;
  --el-color-info: #909399;
}
```

### 3. 添加新的统计维度

#### 场景：需要按职业分类统计

**步骤 1：修改数据库**
在 `accounts` 表添加职业字段：

```sql
ALTER TABLE accounts ADD COLUMN job_type VARCHAR(50);
```

**步骤 2：修改后端**
在 `AccountService` 中添加按职业统计的方法：

```java
public List<IncomeRecord> getRecordsByJobType(Long userId, String jobType, LocalDate startDate, LocalDate endDate) {
    // 实现逻辑
}
```

**步骤 3：修改前端**
在统计页面添加职业筛选：

```vue
<el-select v-model="selectedJobType" @change="loadStatistics">
  <el-option label="全部" :value="null"></el-option>
  <el-option label="剑侠客" value="sword"></el-option>
  <el-option label="方寸山" value="fang"></el-option>
  <!-- 更多职业 -->
</el-select>
```

### 4. 导出功能实现

#### 场景：导出为 Excel 文件

编辑 `backend/src/main/java/com/fy/incometracker/controller/IncomeRecordController.java`，添加导出接口：

```java
@GetMapping("/export/user/{userId}")
public void exportUserRecords(
        @PathVariable Long userId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        HttpServletResponse response) throws IOException {
    
    List<IncomeRecord> records = incomeRecordService.getUserRecordsBetween(userId, startDate, endDate);
    
    // 使用 Apache POI 生成 Excel
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("收益记录");
    
    // 创建表头
    Row headerRow = sheet.createRow(0);
    headerRow.createCell(0).setCellValue("日期");
    headerRow.createCell(1).setCellValue("账号");
    headerRow.createCell(2).setCellValue("总收益");
    // ... 其他列
    
    // 填充数据
    int rowNum = 1;
    for (IncomeRecord record : records) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(record.getRecordDate().toString());
        row.createCell(1).setCellValue(record.getAccountId());
        row.createCell(2).setCellValue(record.getTotalIncome().doubleValue());
        // ... 其他列
    }
    
    // 返回文件
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setHeader("Content-Disposition", "attachment; filename=income_records.xlsx");
    workbook.write(response.getOutputStream());
    workbook.close();
}
```

在前端调用：

```javascript
const exportToExcel = () => {
  window.open(`/api/income-records/export/user/${userId}?startDate=${startDate}&endDate=${endDate}`)
}
```

### 5. 添加图表类型

#### 场景：添加饼图显示收益分布

编辑 `frontend/src/views/Statistics.vue`，修改 `drawChart` 方法：

```javascript
const drawPieChart = (data) => {
  const pieChart = echarts.init(pieContainer.value)
  
  // 计算各类收益合计
  const dungeonTotal = data.reduce((sum, r) => sum + (r.dungeonIncome || 0), 0)
  const stallTotal = data.reduce((sum, r) => sum + (r.stallIncome || 0), 0)
  const taskTotal = data.reduce((sum, r) => sum + (r.taskIncome || 0), 0)
  
  const option = {
    title: {
      text: '收益类型分布'
    },
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        name: '收益分布',
        type: 'pie',
        data: [
          { value: dungeonTotal, name: '副本收益' },
          { value: stallTotal, name: '摆摊收益' },
          { value: taskTotal, name: '任务收益' }
        ]
      }
    ]
  }
  
  pieChart.setOption(option)
}
```

### 6. 性能优化

#### 缓存查询结果

在 `backend/src/main/java/com/fy/incometracker/service/IncomeRecordService.java` 中添加缓存：

```java
import org.springframework.cache.annotation.Cacheable;

@Cacheable(value = "userStatistics", key = "#userId + '-' + #startDate + '-' + #endDate")
public BigDecimal getTotalIncomeByUser(Long userId, LocalDate startDate, LocalDate endDate) {
    // 实现逻辑
}
```

#### 数据库索引优化

在 `docs/database-mysql.sql` 中添加更多索引以加快查询：

```sql
CREATE INDEX idx_income_records_user_account_date 
ON income_records(user_id, account_id, record_date);
```

### 7. 安全加固

#### 修改 JWT 密钥

编辑 `backend/application.yml`：

```yaml
jwt:
  secret: your_very_secure_secret_key_change_this_in_production_environment
```

生成安全的密钥：
```bash
openssl rand -base64 32
```

#### 添加输入验证

在 `backend/src/main/java/com/fy/incometracker/controller/IncomeRecordController.java` 中添加验证注解：

```java
@PostMapping
public ResponseEntity<IncomeRecord> createRecord(@Valid @RequestBody IncomeRecord record) {
    // @Valid 会自动验证注解
}
```

在 `IncomeRecord` 实体中添加验证注解：

```java
@NotNull(message = "总收益不能为空")
@DecimalMin(value = "0", message = "总收益必须大于等于0")
private BigDecimal totalIncome;
```

---

## 常见自定义需求代码示例

### 需求 1：按周统计而不是按月

```java
// backend/service/IncomeRecordService.java
public Map<String, BigDecimal> getWeeklyStatistics(Long userId, LocalDate weekStart) {
    LocalDate weekEnd = weekStart.plusDays(6);
    Map<String, BigDecimal> stats = new HashMap<>();
    
    for (int i = 0; i < 7; i++) {
        LocalDate date = weekStart.plusDays(i);
        BigDecimal daily = incomeRecordRepository
            .findByUserIdAndRecordDate(userId, date)
            .stream()
            .map(IncomeRecord::getNetIncome)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        stats.put(date.toString(), daily);
    }
    
    return stats;
}
```

### 需求 2：按账号分别计算平均收益

```java
public Map<String, BigDecimal> getAverageIncomeByAccount(Long userId, LocalDate startDate, LocalDate endDate) {
    Map<String, BigDecimal> averages = new HashMap<>();
    List<Account> accounts = accountService.getUserAccounts(userId);
    
    for (Account account : accounts) {
        BigDecimal total = getTotalIncomeByAccount(account.getId(), startDate, endDate);
        List<IncomeRecord> records = incomeRecordService.getAccountRecordsBetween(
            account.getId(), startDate, endDate);
        
        BigDecimal average = total.divide(
            new BigDecimal(records.size()), 2, RoundingMode.HALF_UP);
        
        averages.put(account.getAccountName(), average);
    }
    
    return averages;
}
```

### 需求 3：预警机制（收益过低提醒）

```java
public boolean checkLowIncomeWarning(Long userId, LocalDate date) {
    BigDecimal dailyIncome = getTotalIncomeByDay(userId, date);
    BigDecimal threshold = new BigDecimal("500"); // 设置阈值
    
    return dailyIncome.compareTo(threshold) < 0;
}
```

---

## 技术支持

遇到问题？查看这些文件：
- 📖 API 文档：`docs/API.md`
- 🚀 部署指南：`docs/LOCAL_DEPLOYMENT.md`
- 💡 快速开始：`docs/QUICKSTART.md`

祝开发顺利！🎉
