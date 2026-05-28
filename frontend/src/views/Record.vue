<template>
  <div class="record-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>记录收益</span>
          <el-button type="primary" size="small" @click="openBasicIncomeDialog">新增</el-button>
        </div>
      </template>

      <el-form
        :model="form"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :xs="24" :sm="6">
            <el-form-item label="选择账号">
              <el-select
                v-model="form.accountId"
                placeholder="请选择账号"
                @change="onAccountChange"
              >
                <el-option
                  v-for="account in accounts"
                  :key="account.id"
                  :label="account.accountName"
                  :value="account.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="6">
                <el-form-item label="当前经验" class="inline-info">
                  <el-input
                    :model-value="currentAccountInfo.experience"
                    disabled
                    size="small"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="6">
                <el-form-item label="当前金钱" class="inline-info">
                  <el-input
                    :model-value="currentAccountInfo.money"
                    disabled
                    size="small"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
          <el-col
            :xs="24"
            :sm="6"
          >
            <el-form-item label="记录日期">
              <el-date-picker
                v-model="form.recordDate"
                type="date"
                placeholder="选择日期"
              />
            </el-form-item>
          </el-col>
        </el-row>
            <el-row :gutter="20">
              
            </el-row>
        <el-divider>收益信息</el-divider>

        <el-card class="history-card" shadow="hover" style="margin-top: 20px; margin-bottom: 20px;">
          <template #header>
            <div class="card-header">
              <span>收益一览</span>
              <span class="history-summary">
                当前账号：{{ accounts.find((account) => account.id === form.accountId)?.accountName || '未选择' }}
                ｜ 当前日期：{{ formatDate(form.recordDate) || '未选择' }}
              </span>
            </div>
          </template>

          <el-table
            :data="historyRecords"
            border
            stripe
            empty-text="当前账号与日期下暂无收益记录"
          >
            <el-table-column label="提交时间" width="170">
              <template #default="{ row }">
                {{ formatTimestamp(row.createdAt) }}
              </template>
            </el-table-column>
            <el-table-column label="任务类型" min-width="220">
              <template #default="{ row }">
                {{ row.taskType || '-' }}
              </template>
            </el-table-column>
            <el-table-column label="明细" min-width="260">
              <template #default="{ row }">
                {{ row.summary || '-' }}
              </template>
            </el-table-column>
            <el-table-column label="经验收益" prop="totalExperience" width="120" align="right" />
            <el-table-column label="金钱收益" width="140" align="right">
              <template #default="{ row }">
                {{ Number(row.totalMoney || 0).toFixed(2) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="140" align="center">
              <template #default="{ row }">
                <el-button
                  type="primary"
                  link
                  @click="openEditRecord(row)"
                >
                  编辑
                </el-button>
                <el-button
                  type="danger"
                  link
                  @click="deleteRecord(row)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
<!-- 
        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            @click="submitForm"
          >
            提交记录
          </el-button>
          <el-button @click="resetForm">
            重置
          </el-button>
        </el-form-item> -->
      </el-form>
    </el-card>

    <el-dialog
      v-model="basicIncomeDialogVisible"
      title="新增基本收益"
      width="860px"
      :close-on-click-modal="false"
    >
      <el-form :model="basicIncomeForm" label-width="100px">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12">
            <el-form-item label="类型">
              <el-select
                v-model="basicIncomeForm.category"
                placeholder="请选择类型"
                @change="onBasicIncomeCategoryChange"
              >
                <el-option
                  v-for="cat in incomeCategories"
                  :key="cat"
                  :label="cat"
                  :value="cat"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="任务类型">
              <el-select
                v-model="basicIncomeForm.incomeId"
                placeholder="请选择任务类型"
                :disabled="!basicIncomeForm.category"
              >
                <el-option
                  v-for="item in incomeTypes.filter(i => i.type === basicIncomeForm.category)"
                  :key="item.id"
                  :label="item.typeName"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :xs="24" :sm="12">
            <el-form-item label="开始时间">
              <el-time-picker
                v-model="basicIncomeForm.startTime"
                placeholder="开始"
                format="HH:mm"
                value-format="HH:mm"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="结束时间">
              <el-time-picker
                v-model="basicIncomeForm.endTime"
                placeholder="结束"
                format="HH:mm"
                value-format="HH:mm"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :xs="24" :sm="12">
            <el-form-item label="经验">
              <el-input
                v-model.number="basicIncomeForm.experience"
                type="number"
                placeholder="请输入经验"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="金钱 (两)">
              <el-input
                v-model.number="basicIncomeForm.money"
                type="number"
                placeholder="请输入金钱"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider>物品奖励</el-divider>

        <div class="reward-list">
          <div
            v-for="(reward, index) in basicIncomeForm.itemRewards"
            :key="index"
            class="reward-row"
          >
            <el-select
              v-model="reward.itemType"
              placeholder="请选择类型"
              size="small"
              style="width: 100px"
              @change="() => onRewardTypeChange(reward)"
            >
              <el-option
                v-for="type in moneyItemTypes"
                :key="type"
                :label="type"
                :value="type"
              />
            </el-select>
            <el-select
              v-model="reward.itemId"
              placeholder="请选择物品"
              size="small"
              style="width: 120px"
              :disabled="!reward.itemType"
              @change="(val) => onRewardSelectChange(val, reward)"
            >
              <el-option
                v-for="item in moneyItems.filter(x => x.itemType === reward.itemType)"
                :key="item.id"
                :label="item.itemName"
                :value="item.id"
              />
            </el-select>
            <el-input-number
              v-model.number="reward.quantity"
              :min="1"
              size="small"
              style="width: 100px"
            />
            <el-input
              v-model.number="reward.money"
              type="number"
              size="small"
              placeholder="单价"
              style="width: 100px"
            />
            <el-button
              type="danger"
              size="small"
              @click="removeReward(basicIncomeForm, reward)"
            >
              删
            </el-button>
          </div>
        </div>

        <el-button type="primary" link @click="addReward(basicIncomeForm)">添加物品</el-button>

        <el-divider>成本信息</el-divider>

        <el-row :gutter="20">
          <el-col :xs="24" :sm="12">
            <el-form-item label="点卡 (小时)">
              <el-input
                v-model.number="basicIncomeForm.cardCostHours"
                type="number"
                placeholder="请输入点卡消耗时长"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="时间 (分钟)">
              <el-input
                v-model.number="basicIncomeForm.timeSpent"
                type="number"
                placeholder="请输入花费时间"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注">
          <el-input
            v-model="basicIncomeForm.notes"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="basicIncomeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveBasicIncomeDraft">保存收益</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'
import { roundMoney } from '../utils/format'

const loading = ref(false)
const accounts = ref([])
const moneyItems = ref([])
const incomeTypes = ref([])

const currentAccountInfo = ref({
  experience: 0,
  money: 0
})

const extractedValues = ref({
  expStart: null,
  expEnd: null,
  moneyStart: null,
  moneyEnd: null
})

const expDelta = computed(() => {
  if (extractedValues.value.expStart == null || extractedValues.value.expEnd == null) {
    return null
  }
  return extractedValues.value.expEnd - extractedValues.value.expStart
})

const moneyDelta = computed(() => {
  if (extractedValues.value.moneyStart == null || extractedValues.value.moneyEnd == null) {
    return null
  }
  return extractedValues.value.moneyEnd - extractedValues.value.moneyStart
})

const incomeCategories = computed(() => {
  const cats = incomeTypes.value.map(i => i.type)
  return Array.from(new Set(cats))
})

const moneyItemTypes = computed(() => {
  const types = moneyItems.value.map(m => m.itemType)
  return Array.from(new Set(types))
})

const getBasicIncomeLabel = (item) => {
  const typeItem = incomeTypes.value.find(i => i.id === item.incomeId)
  return typeItem ? `${typeItem.typeName}` : '未选择'
}

const summarizeRewards = (entry) => {
  if (!Array.isArray(entry.itemRewards) || entry.itemRewards.length === 0) {
    if (entry.itemName) {
      return `${entry.itemName} x${entry.quantity || 1}`
    }
    return ''
  }

  return entry.itemRewards.map((reward) => `${reward.itemName || '未知物品'} x${reward.quantity || 1}`).join('、')
}

const buildHistorySummary = (entry) => {
  const summary = summarizeRewards(entry)
  return summary || `${entry.activityType || entry.type || '收益'}`
}

const openBasicIncomeDialog = () => {
  basicIncomeForm.value = {
    category: null,
    incomeId: null,
    experience: 0,
    money: 0,
    startTime: '',
    endTime: '',
    itemRewards: [],
    cardCostHours: Number(form.value.cardCostHours || 0),
    timeSpent: Number(form.value.timeSpent || 0),
    notes: form.value.notes || ''
  }
  basicIncomeDialogVisible.value = true
}

const onBasicIncomeCategoryChange = () => {
  basicIncomeForm.value.incomeId = null
}

const persistCurrentRecord = async () => {
  if (!form.value.accountId) {
    ElMessage.warning('请先选择账号')
    return
  }

  const recordDate = formatDate(form.value.recordDate)
  if (!recordDate) {
    ElMessage.warning('请选择有效的记录日期')
    return
  }

  loading.value = true
  try {
    const incomeRecords = form.value.basicIncomeRecords.map((row) => {
      const typeItem = incomeTypes.value.find(i => i.id === row.incomeId)
      return {
        type: row.category,
        activityType: typeItem?.typeName || '',
        activityTypeId: row.incomeId,
        experience: row.experience,
        money: row.money,
        startTime: row.startTime || null,
        endTime: row.endTime || null,
        itemRewards: (row.itemRewards || []).map((reward) => ({
          itemType: reward.itemType,
          itemId: reward.itemId,
          itemName: reward.itemName,
          level: reward.level,
          quantity: reward.quantity,
          money: reward.money
        }))
      }
    })

    const raw = {
      ...form.value,
      recordDate,
      totalIncome: calculateTotalIncome(),
      incomeRecords
    }

    const payload = JSON.parse(JSON.stringify(raw))
    let response
    if (editingRecordId.value) {
      response = await request.put(`/income-records/${editingRecordId.value}`, payload)
      ElMessage.success('记录更新成功')
    } else {
      response = await request.post('/income-records', payload)
      ElMessage.success('收益保存成功')
    }

    await loadRecordHistory()
    resetForm()
    return response
  } catch (error) {
    console.error('保存失败:', error)
    const message = error?.response?.data?.message || error.message || '未知错误'
    ElMessage.error('保存失败: ' + message)
  } finally {
    loading.value = false
  }
}

const saveBasicIncomeDraft = async () => {
  if (!form.value.accountId) {
    ElMessage.warning('请先选择账号')
    return
  }

  if (!basicIncomeForm.value.category || !basicIncomeForm.value.incomeId) {
    ElMessage.warning('请完整选择收益类型和任务类型')
    return
  }

  const newEntry = {
    category: basicIncomeForm.value.category,
    incomeId: basicIncomeForm.value.incomeId,
    experience: Number(basicIncomeForm.value.experience || 0),
    money: Number(basicIncomeForm.value.money || 0),
    startTime: basicIncomeForm.value.startTime || '',
    endTime: basicIncomeForm.value.endTime || '',
    itemRewards: (basicIncomeForm.value.itemRewards || []).map((reward) => ({
      itemType: reward.itemType,
      itemId: reward.itemId,
      itemName: reward.itemName,
      level: reward.level,
      quantity: Number(reward.quantity || 1),
      money: Number(reward.money || 0)
    }))
  }

  form.value.basicIncomeRecords.push(newEntry)
  form.value.cardCostHours = Number(basicIncomeForm.value.cardCostHours || 0)
  form.value.timeSpent = Number(basicIncomeForm.value.timeSpent || 0)
  form.value.notes = basicIncomeForm.value.notes || ''

  basicIncomeDialogVisible.value = false
  await persistCurrentRecord()
}

/**
 * 向指定行的奖励列表中添加一个新的奖励项
 * @param {Object} row - 包含奖励数据的行对象
 */
const addReward = (row) => {
  // 确保 itemRewards 数组已初始化
  if (!row.itemRewards) {
    row.itemRewards = []
  }
  row.itemRewards.push({ itemType: null, itemId: null, itemName: '', level: '', quantity: 1, money: 0 })
}

const removeReward = (row, reward) => {
  const index = row.itemRewards.indexOf(reward)
  if (index !== -1) {
    row.itemRewards.splice(index, 1)
  }
}

/**
 * 当奖励类型变更时，重置奖励相关的字段数据
 * @param {Object} reward - 奖励对象
 */
const onRewardTypeChange = (reward) => {
  // 重置奖励关联的物品ID、名称、等级及金额
  reward.itemId = null
  reward.itemName = ''
  reward.level = ''
  reward.money = 0
}

const onRewardSelectChange = (val, reward) => {
  const mi = moneyItems.value.find((m) => m.id === val)
  if (mi) {
    reward.itemName = mi.itemName
    reward.level = mi.level || reward.level || ''
    reward.money = mi.moneyValue || 0
  } else {
    reward.itemName = ''
    reward.level = ''
    reward.money = 0
  }
}

const historyRecords = ref([])
const editingRecordId = ref(null)
const basicIncomeDialogVisible = ref(false)
const basicIncomeForm = ref({
  category: null,
  incomeId: null,
  experience: 0,
  money: 0,
  startTime: '',
  endTime: '',
  itemRewards: [],
  cardCostHours: 0,
  timeSpent: 0,
  notes: ''
})

const formatTimestamp = (value) => {
  if (!value) return '-'
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return value
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  }).replace(/\//g, '-')
}

const loadRecordHistory = async () => {
  if (!form.value.accountId) {
    historyRecords.value = []
    return
  }

  const recordDate = formatDate(form.value.recordDate)
  if (!recordDate) {
    historyRecords.value = []
    return
  }

  try {
    const response = await request.get('/income-records', {
      params: {
        accountId: form.value.accountId,
        startDate: recordDate,
        endDate: recordDate
      }
    })

    historyRecords.value = (Array.isArray(response) ? response : []).map((record) => {
      const totalExperience = (record.incomeEntries || []).reduce((sum, entry) => sum + Number(entry.experience || 0), 0)
      const totalMoney = Number(record.netIncome ?? record.totalIncome ?? 0)
      const taskType = Array.from(new Set((record.incomeEntries || []).map((entry) => entry.activityType || entry.type || '未知收益').filter(Boolean))).join('、') || '-'
      const summary = (record.incomeEntries || []).map((entry) => {
        const rewardSummary = summarizeRewards(entry)
        if (rewardSummary) {
          return `${entry.activityType || entry.type || '收益'}: ${rewardSummary}`
        }
        return entry.activityType || entry.detail || entry.type || '未知收益'
      }).join('、') || '-'

      return {
        ...record,
        totalExperience,
        totalMoney,
        taskType,
        summary
      }
    })
  } catch (error) {
    console.error('加载收益一览失败:', error)
    historyRecords.value = []
  }
}

const openEditRecord = (record) => {
  editingRecordId.value = record.id
  form.value.accountId = record.accountId
  form.value.recordDate = record.recordDate ? new Date(record.recordDate) : new Date()
  form.value.timeSpent = record.timeSpent || 0
  form.value.notes = record.notes || ''

  const incomeEntries = Array.isArray(record.incomeEntries) ? record.incomeEntries : []
  form.value.basicIncomeRecords = incomeEntries.map((entry) => ({
    category: entry.type,
    incomeId: entry.activityTypeId,
    experience: Number(entry.experience || 0),
    money: Number(entry.money || 0),
    startTime: entry.startTime || '',
    endTime: entry.endTime || '',
    itemRewards: Array.isArray(entry.itemRewards)
      ? entry.itemRewards.map((item) => ({
          itemType: item.itemType || entry.type || '',
          itemId: item.itemId,
          itemName: item.itemName || '',
          level: item.level || '',
          quantity: Number(item.quantity || 1),
          money: Number(item.money || 0)
        }))
      : (entry.itemName || entry.itemId || entry.quantity || entry.level) ? [{
          itemType: entry.type,
          itemId: entry.itemId,
          itemName: entry.itemName || '',
          level: entry.level || '',
          quantity: Number(entry.quantity || 1),
          money: Number(entry.money || 0)
        }] : []
  }))
}

const deleteRecord = (record) => {
  if (!record?.id) {
    return
  }

  ElMessageBox.confirm('确认删除这条收益记录吗？', '提示', {
    confirmButtonText: '删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/income-records/${record.id}`)
      ElMessage.success('删除成功')
      await loadRecordHistory()
    } catch (error) {
      console.error('删除收益记录失败:', error)
      ElMessage.error('删除失败: ' + (error.response?.data?.message || error.message))
    }
  })
}

const onBasicCategoryChange = (row) => {
  row.incomeId = null
}


const onAccountChange = (accountId) => {
  const account = accounts.value.find(a => a.id === accountId)
  if (account) {
    currentAccountInfo.value = {
      experience: account.currentExperience || 0,
      money: account.startingMoney || 0
    }
  } else {
    currentAccountInfo.value = {
      experience: 0,
      money: 0
    }
  }
}

const loadIncomeTypes = async () => {
  try {
    incomeTypes.value = await request.get('/system/income-types')
  } catch (error) {
    console.error('加载收益类型失败:', error)
  }
}

const form = ref({
  accountId: null,
  recordDate: new Date(),
  basicIncomeRecords: [],
  cardCostHours: 0,
  cardCostPerHour: 0,
  cardCostRate: 0,
  cardCost: 0,
  netIncome: 0,
  timeSpent: 0,
  notes: ''
})

// 自动计算点卡消费
watch(
  () => ({
    cardCostHours: form.value.cardCostHours,
    cardCostPerHour: form.value.cardCostPerHour,
    cardCostRate: form.value.cardCostRate
  }),
  () => {
    form.value.cardCost = roundMoney((form.value.cardCostHours || 0) * (form.value.cardCostPerHour || 0) * (form.value.cardCostRate || 0))
  },
  { deep: true }
)

// 计算总收益
const calculateTotalIncome = () => {
  return form.value.basicIncomeRecords.reduce((sum, record) => {
    const rewardTotal = (record.itemRewards || []).reduce((rewardSum, reward) => {
      return rewardSum + ((reward.money || 0) * (reward.quantity || 1))
    }, 0)
    return sum + (record.money || 0) + rewardTotal
  }, 0)
}

// 监听成本变化，自动计算净收益
watch(
  () => ({
    basicIncomeRecords: form.value.basicIncomeRecords,
    itemCost: form.value.itemCost,
    goldCost: form.value.goldCost,
    cardCost: form.value.cardCost,
    otherCost: form.value.otherCost
  }),
  () => {
    const totalCost = (form.value.itemCost || 0) + (form.value.goldCost || 0) + (form.value.cardCost || 0) + (form.value.otherCost || 0)
    const totalIncome = calculateTotalIncome()
    form.value.netIncome = roundMoney(totalIncome - totalCost)
  },
  { deep: true }
)

/**
 * 异步加载账户列表数据
 * 
 * @returns {Promise<void>} 无返回值，结果直接更新到 accounts 响应式变量中
 */
const loadAccounts = async () => {
  try {
    // 请求账户接口并更新本地状态
    const response = await request.get('/accounts')
    accounts.value = response
  } catch (error) {
    console.error('加载账号失败:', error)
  }
}

const loadMoneyItems = async () => {
  try {
    moneyItems.value = await request.get('/system/money-items')
  } catch (error) {
    console.error('加载物品奖励列表失败:', error)
  }
}

const loadPointCard = async () => {
  try {
    const pointCard = await request.get('/system/point-card')
    if (pointCard) {
      form.value.cardCostPerHour = pointCard.pointsPerHour || 0
      form.value.cardCostRate = pointCard.exchangeRate || 0
    }
  } catch (error) {
    console.error('加载点卡信息失败:', error)
  }
}

const formatDate = (date) => {
  if (!date) return null
  if (typeof date === 'string') return date
  if (!(date instanceof Date)) return null
  const pad = (value) => String(value).padStart(2, '0')
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}`
}

watch(
  () => [form.value.accountId, formatDate(form.value.recordDate)],
  ([accountId, recordDate]) => {
    if (accountId && recordDate) {
      loadRecordHistory()
    } else {
      historyRecords.value = []
    }
  },
  { immediate: true }
)

/**
 * 提交收益记录表单
 * 
 * 该函数负责验证表单数据，构建收益记录 payload，并根据是否存在现有记录执行更新或创建操作。
 * 
 * @async
 * @function submitForm
 * @returns {Promise<void>} 无返回值，通过消息提示反馈操作结果
 */
const submitForm = async () => {
  if (form.value.basicIncomeRecords.length === 0) {
    ElMessage.warning('请先保存收益明细')
    return
  }

  await persistCurrentRecord()
}

const resetForm = () => {
  const currentCardCostPerHour = form.value.cardCostPerHour
  const currentCardCostRate = form.value.cardCostRate
  const currentAccountId = form.value.accountId
  const currentRecordDate = form.value.recordDate
  editingRecordId.value = null

  form.value = {
    accountId: currentAccountId,
    recordDate: currentRecordDate,
    basicIncomeRecords: [],
    itemCost: 0,
    goldCost: 0,
    cardCostHours: 0,
    cardCostPerHour: currentCardCostPerHour,
    cardCostRate: currentCardCostRate,
    cardCost: 0,
    otherCost: 0,
    netIncome: 0,
    timeSpent: 0,
    notes: ''
  }
}

onMounted(() => {
  loadAccounts()
  loadMoneyItems()
  loadIncomeTypes()
  loadPointCard()
})
</script>

<style scoped>
.record-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.history-summary {
  font-size: 12px;
  color: #909399;
}

:deep(.el-input__wrapper) {
  max-width: 200px;
}

:deep(.el-form-item__content) {
  max-width: 200px;
}

.income-card {
  display: flex;
  flex-direction: column;
  height: 100%;
}

:deep(.income-card .el-card__body) {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.income-label {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
}

.income-input {
  flex: 1;
}

:deep(.income-input .el-input__wrapper) {
  max-width: 100%;
}

.income-value {
  font-size: 18px;
  font-weight: bold;
  color: #409eff;
  text-align: center;
  padding: 8px 0;
}

.info-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding: 0;
  border: none;
  background: transparent;
  min-height: 34px;
}

.info-label {
  color: #606266;
  margin-right: 8px;
}

.info-value {
  font-weight: 600;
  color: #303133;
}

.preview-tag {
  color: #909399;
  font-size: 12px;
}
</style>