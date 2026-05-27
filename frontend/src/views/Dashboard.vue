<template>
  <div class="dashboard-page">
    <div class="cards-container">
      <el-row :gutter="20">
        <el-col
          :xs="24"
          :sm="12"
          :md="6"
        >
          <el-card class="stat-card">
            <template #header>
              <div class="card-header">
                <span>今日净收益</span>
              </div>
            </template>
            <div class="stat-value">
              {{ formatTwoWan(todayNetIncome) }}
            </div>
          </el-card>
        </el-col>
        <el-col
          :xs="24"
          :sm="12"
          :md="6"
        >
          <el-card class="stat-card">
            <template #header>
              <div class="card-header">
                <span>本周净收益</span>
              </div>
            </template>
            <div class="stat-value">
              {{ formatTwoWan(weekNetIncome) }}
            </div>
          </el-card>
        </el-col>
        <el-col
          :xs="24"
          :sm="12"
          :md="6"
        >
          <el-card class="stat-card">
            <template #header>
              <div class="card-header">
                <span>本月净收益</span>
              </div>
            </template>
            <div class="stat-value">
              {{ formatTwoWan(monthNetIncome) }}
            </div>
          </el-card>
        </el-col>
        <el-col
          :xs="24"
          :sm="12"
          :md="6"
        >
          <el-card class="stat-card">
            <template #header>
              <div class="card-header">
                <span>账号数量</span>
              </div>
            </template>
            <div class="stat-value">
              {{ accountCount }}
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <el-card style="margin-top: 20px">
      <template #header>
        <div class="card-header">
          <span>最近收益记录</span>
        </div>
      </template>
      <el-table
        :data="paginatedRecentRecords"
        stripe
        style="width: 100%"
      >
        <el-table-column
          prop="accountName"
          label="账号"
          width="100"
        />
        <el-table-column
          prop="recordDate"
          label="日期"
          width="120"
        />
        <el-table-column
          label="总收益"
          width="120"
        >
          <template #default="{ row }">
            {{ formatTwoWan(row.totalIncome) }}
          </template>
        </el-table-column>
        <el-table-column
          label="净收益"
          width="120"
        >
          <template #default="{ row }">
            {{ formatTwoWan(row.netIncome) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="notes"
          label="备注"
        >       
          <template #default="{ row }">
            {{ row.notes }}
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 20px; text-align: right;">
        <el-pagination
          background
          layout="prev, pager, next"
          :current-page="recentRecordsPage"
          :page-size="recentRecordsPageSize"
          :total="recentRecords.length"
          @current-change="(page) => recentRecordsPage = page"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import request from '../utils/request'
import { formatTwoWan, roundMoney } from '../utils/format'

const todayNetIncome = ref(0)
const weekNetIncome = ref(0)
const monthNetIncome = ref(0)
const accountCount = ref(0)
const recentRecords = ref([])
const recentRecordsPage = ref(1)
const recentRecordsPageSize = 10

const paginatedRecentRecords = computed(() => {
  const start = (recentRecordsPage.value - 1) * recentRecordsPageSize
  return recentRecords.value.slice(start, start + recentRecordsPageSize)
})

const formatDate = (date) => {
  return date.toISOString().split('T')[0]
}

const loadData = async () => {
  try {
    const [accounts, records] = await Promise.all([
      request.get('/accounts'),
      request.get('/income-records')
    ])

    // 创建账号ID到名称的映射
    const accountMap = {}
    accounts.forEach(account => {
      accountMap[account.id] = account.accountName
    })

    accountCount.value = accounts.length
    recentRecords.value = records
      .slice(-5)
      .reverse()
      .map(item => ({
        ...item,
        recordDate: item.recordDate,
        accountName: accountMap[item.accountId] || '未知账号'
      }))

    const today = formatDate(new Date())
    const now = new Date()
    const startOfWeek = new Date(now)
    startOfWeek.setDate(now.getDate() - (now.getDay() || 7) + 1)
    const startOfMonth = new Date(now.getFullYear(), now.getMonth(), 1)

    const todayRecords = records.filter(r => r.recordDate === today)
    const weekRecords = records.filter(r => r.recordDate >= formatDate(startOfWeek))
    const monthRecords = records.filter(r => r.recordDate >= formatDate(startOfMonth))

    todayNetIncome.value = roundMoney(todayRecords.reduce((sum, r) => sum + (r.netIncome || 0), 0))
    weekNetIncome.value = roundMoney(weekRecords.reduce((sum, r) => sum + (r.netIncome || 0), 0))
    monthNetIncome.value = roundMoney(monthRecords.reduce((sum, r) => sum + (r.netIncome || 0), 0))
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.dashboard-page {
  width: 100%;
}

.cards-container {
  margin-bottom: 20px;
}

.stat-card {
  height: 100%;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
  text-align: center;
  padding: 20px 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
