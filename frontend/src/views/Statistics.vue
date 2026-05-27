<template>
  <div class="statistics-container">
    <el-row
      :gutter="20"
      style="margin-bottom: 20px"
    >
      <el-col
        :xs="24"
        :sm="12"
        :md="6"
      >
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="loadStatistics"
        />
      </el-col>
      <el-col
        :xs="24"
        :sm="12"
        :md="6"
      >
        <el-select
          v-model="selectedAccount"
          placeholder="选择账号统计"
          @change="loadStatistics"
        >
          <el-option
            label="全部账号"
            :value="null"
          />
          <el-option
            v-for="account in accounts"
            :key="account.id"
            :label="account.accountName"
            :value="account.id"
          />
        </el-select>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col
        :xs="24"
        :sm="12"
        :md="6"
      >
        <el-card class="stat-card">
          <template #header>
            <div class="card-header">
              <span>总收益</span>
            </div>
          </template>
          <div class="stat-value">
            {{ formatTwoWan(totalIncome) }}
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
              <span>总成本</span>
            </div>
          </template>
          <div class="stat-value">
            {{ formatTwoWan(totalCost) }}
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
              <span>净收益</span>
            </div>
          </template>
          <div class="stat-value">
            {{ formatTwoWan(totalNetIncome) }}
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
              <span>记录数</span>
            </div>
          </template>
          <div class="stat-value">
            {{ records.length }}
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 20px">
      <template #header>
        <div class="card-header">
          <span>收益趋势</span>
          <el-button
            type="success"
            size="small"
            @click="exportToExcel"
          >
            导出 Excel
          </el-button>
        </div>
      </template>
      <div
        ref="chartContainer"
        style="width: 100%; height: 400px;"
      />
    </el-card>

    <el-card style="margin-top: 20px">
      <template #header>
        <div class="card-header">
          <span>详细记录</span>
        </div>
      </template>
      <el-table
        :data="paginatedRecords"
        stripe
        style="width: 100%"
      >
        <el-table-column
          prop="recordDate"
          label="日期"
          width="120"
        />
        <el-table-column
          prop="accountName"
          label="账号"
          width="100"
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
          label="物品成本"
          width="120"
        >
          <template #default="{ row }">
            {{ formatTwoWan(row.itemCost) }}
          </template>
        </el-table-column>
        <el-table-column
          label="金币成本"
          width="120"
        >
          <template #default="{ row }">
            {{ formatTwoWan(row.goldCost) }}
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
          prop="timeSpent"
          label="时间"
          width="80"
        />
        <el-table-column
          prop="notes"
          label="备注"
        />
      </el-table>
      <div style="margin-top: 20px; text-align: right;">
        <el-pagination
          background
          layout="prev, pager, next"
          :current-page="recordsPage"
          :page-size="recordsPageSize"
          :total="records.length"
          @current-change="(page) => recordsPage = page"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'
import * as echarts from 'echarts'
import { formatTwoWan, roundMoney } from '../utils/format'

const chartContainer = ref(null)
let chart = null
const accounts = ref([])
const records = ref([])
const recordsPage = ref(1)
const recordsPageSize = 10
const dateRange = ref([new Date(new Date().getFullYear(), new Date().getMonth(), 1), new Date()])
const selectedAccount = ref(null)
const totalIncome = ref(0)
const totalCost = ref(0)
const totalNetIncome = ref(0)

const paginatedRecords = computed(() => {
  const start = (recordsPage.value - 1) * recordsPageSize
  return records.value.slice(start, start + recordsPageSize)
})

const loadStatistics = async () => {
  try {
    const [startDate, endDate] = dateRange.value
    
    if (!startDate || !endDate) {
      return
    }

    const start = startDate.toISOString().split('T')[0]
    const end = endDate.toISOString().split('T')[0]

    const params = {
      startDate: start,
      endDate: end
    }
    if (selectedAccount.value) {
      params.accountId = selectedAccount.value
    }

    const response = await request.get('/income-records', { params })

    let filteredRecords = response
    if (selectedAccount.value) {
      filteredRecords = response.filter(r => r.accountId === selectedAccount.value)
    }

    records.value = filteredRecords

    // 计算统计数据
    totalIncome.value = roundMoney(filteredRecords.reduce((sum, r) => sum + (r.totalIncome || 0), 0))
    const totalItemCost = filteredRecords.reduce((sum, r) => sum + (r.itemCost || 0), 0)
    const totalGoldCost = filteredRecords.reduce((sum, r) => sum + (r.goldCost || 0), 0)
    const totalOtherCost = filteredRecords.reduce((sum, r) => sum + (r.otherCost || 0), 0)
    totalCost.value = roundMoney(totalItemCost + totalGoldCost + totalOtherCost)
    totalNetIncome.value = roundMoney(filteredRecords.reduce((sum, r) => sum + (r.netIncome || 0), 0))

    drawChart(filteredRecords)
  } catch (error) {
    console.error('加载统计数据失败:', error)
    ElMessage.error('加载数据失败')
  }
}

const loadAccounts = async () => {
  try {
    const response = await request.get('/accounts')
    accounts.value = response
  } catch (error) {
    console.error('加载账号失败:', error)
  }
}

const drawChart = (data) => {
  if (!chart) {
    chart = echarts.init(chartContainer.value)
  }

  const dates = data.map(r => r.recordDate).sort()
  const incomes = data.map(r => r.totalIncome || 0)
  const netIncomes = data.map(r => r.netIncome || 0)

  const option = {
    title: {
      text: '收益趋势图'
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['总收益', '净收益']
    },
    xAxis: {
      type: 'category',
      data: dates
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '总收益',
        data: incomes,
        type: 'line',
        smooth: true
      },
      {
        name: '净收益',
        data: netIncomes,
        type: 'line',
        smooth: true
      }
    ]
  }

  chart.setOption(option)
}

const exportToExcel = () => {
  ElMessage.info('导出功能开发中...')
}

onMounted(() => {
  loadAccounts()
  loadStatistics()

  window.addEventListener('resize', () => {
    chart?.resize()
  })
})
</script>

<style scoped>
.statistics-container {
  padding: 20px;
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
