<template>
  <div class="summary-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>收益汇总</span>
          <el-button type="primary" size="small" @click="loadAggregatedStats">刷新</el-button>
        </div>
      </template>

      <el-row :gutter="20" style="margin-bottom: 20px">
        <el-col :xs="24" :sm="12">
          <el-card class="stat-card">
            <div class="stat-label">总收益</div>
            <div class="stat-value">{{ totalIncome }} 两</div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="12">
          <el-card class="stat-card">
            <div class="stat-label">记录条数</div>
            <div class="stat-value">{{ recordCount }}</div>
          </el-card>
        </el-col>
      </el-row>

      <el-table :data="paginatedDetailStats" stripe style="width: 100%">
        <el-table-column prop="accountName" label="账号" width="180" />
        <el-table-column prop="type" label="类型" width="160" />
        <el-table-column prop="activityType" label="活动类型" width="200" />
        <el-table-column prop="experience" label="经验" width="120" />
        <el-table-column prop="money" label="金钱 (两)" width="140" />
      </el-table>
      <div style="margin-top: 20px; text-align: right;">
        <el-pagination
          background
          layout="prev, pager, next"
          :current-page="detailStatsPage"
          :page-size="detailStatsPageSize"
          :total="detailStats.length"
          @current-change="(page) => detailStatsPage = page"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import request from '../utils/request'

const detailStats = ref([])
const detailStatsPage = ref(1)
const detailStatsPageSize = 10
const totalIncome = ref(0)
const recordCount = ref(0)

const paginatedDetailStats = computed(() => {
  const start = (detailStatsPage.value - 1) * detailStatsPageSize
  return detailStats.value.slice(start, start + detailStatsPageSize)
})

/**
 * 加载并聚合收益统计数据
 * 
 * 该函数异步获取账户列表和收入记录，将数据按账户名、收入类型和活动类型进行分组聚合，
 * 计算每个分组的总经验和总金额，同时统计全局总收入和记录总数。
 * 最终结果会经过排序后更新到响应式变量中。
 * 
 * @async
 * @returns {Promise<void>} 无返回值，直接更新内部响应式状态
 */
const loadAggregatedStats = async () => {
  try {
    // 并行请求账户信息和收入记录以提高加载效率
    const [accounts, records] = await Promise.all([
      request.get('/accounts'),
      request.get('/income-records')
    ])

    // 构建账户ID到账户名称的映射表，便于后续快速查找
    const accountMap = {}
    accounts.forEach((account) => {
      accountMap[account.id] = account.accountName
    })

    const grouped = {}
    let total = 0
    let count = 0

    // 遍历收入记录，按账户、类型和活动类型进行数据聚合
    records.forEach((record) => {
      if (!Array.isArray(record.incomeRecords)) {
        return
      }
      const accountName = accountMap[record.accountId] || '未知账号'
      record.incomeRecords.forEach((income) => {
        const type = income.type || '未知类型'
        const activityType = income.activityType || '-'
        const key = `${accountName}__${type}__${activityType}`
        if (!grouped[key]) {
          grouped[key] = {
            accountName,
            type,
            activityType,
            experience: 0,
            money: 0
          }
        }
        grouped[key].experience += income.experience || 0
        grouped[key].money += income.money || 0
        total += income.money || 0
        count += 1
      })
    })

    // 对聚合后的数据进行多级排序：优先按账户名，其次按类型，最后按活动类型
    detailStats.value = Object.values(grouped).sort((a, b) => {
      // 先按账号排序
      const accountCompare = a.accountName.localeCompare(b.accountName)
      if (accountCompare !== 0) return accountCompare
      // 再按类型排序
      const typeCompare = a.type.localeCompare(b.type)
      if (typeCompare !== 0) return typeCompare
      // 最后按活动类型排序
      return a.activityType.localeCompare(b.activityType)
    })
    totalIncome.value = total
    recordCount.value = count
  } catch (error) {
    console.error('加载收益汇总失败:', error)
  }
}

onMounted(() => {
  loadAggregatedStats()
})
</script>

<style scoped>
.summary-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-card {
  text-align: center;
}

.stat-label {
  color: #909399;
  font-size: 14px;
}

.stat-value {
  font-size: 24px;
  margin-top: 8px;
  color: #409eff;
  font-weight: bold;
}
</style>
