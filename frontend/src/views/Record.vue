<template>
  <div class="record-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>记录收益</span>
        </div>
      </template>

      <el-form
        :model="form"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col
            :xs="24"
            :sm="12"
          >
            <el-form-item label="选择账号">
              <el-select
                v-model="form.accountId"
                placeholder="请选择账号"
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
          <el-col
            :xs="24"
            :sm="12"
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

        <el-divider>收益信息</el-divider>

        <el-row :gutter="20" style="margin-bottom: 20px">
          <el-col :xs="24">
            <el-card class="income-card">
              <template #header>
                <div class="card-header">
                  <span>基本收益</span>
                  <el-button type="primary" size="small" @click="addBasicIncome">新增</el-button>
                </div>
              </template>

              <el-table
                :data="form.basicIncomeRecords"
                stripe
                style="width: 100%"
                max-height="400"
              >
                <el-table-column
                  label="类型"
                  width="160"
                >
                  <template #default="{ row }">
                    <el-select
                      v-model="row.category"
                      placeholder="请选择类型"
                      size="small"
                      @change="() => onBasicCategoryChange(row)"
                    >
                      <el-option
                        v-for="cat in incomeCategories"
                        :key="cat"
                        :label="cat"
                        :value="cat"
                      />
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column
                  label="活动类型"
                  width="220"
                >
                  <template #default="{ row }">
                    <el-select
                      v-model="row.incomeId"
                      placeholder="请选择活动类型"
                      size="small"
                      :disabled="!row.category"
                    >
                      <el-option
                        v-for="item in incomeTypes.filter(i => i.type === row.category)"
                        :key="item.id"
                        :label="item.typeName"
                        :value="item.id"
                      />
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column
                  label="经验"
                  width="120"
                >
                  <template #default="{ row }">
                    <el-input-number
                      v-model.number="row.experience"
                      :min="0"
                      size="small"
                      style="width: 100%"
                    />
                  </template>
                </el-table-column>
                <el-table-column
                  label="金钱 (两)"
                  width="140"
                >
                  <template #default="{ row }">
                    <el-input-number
                      v-model.number="row.money"
                      :min="0"
                      size="small"
                      style="width: 100%"
                    />
                  </template>
                </el-table-column>
                <el-table-column
                  label="操作"
                  width="100"
                >
                  <template #default="{ row }">
                    <el-button
                      type="danger"
                      size="small"
                      @click="removeBasicIncome(row)"
                    >
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </el-col>
        </el-row>

        <el-row :gutter="20" style="margin-bottom: 20px">
          <el-col :xs="24">
            <el-card class="income-card">
              <template #header>
                <div class="card-header">
                  <span>物品收益</span>
                  <el-button type="primary" size="small" @click="addItemIncome">新增</el-button>
                </div>
              </template>

              <el-table
                :data="form.itemIncomeRecords"
                stripe
                style="width: 100%"
                max-height="400"
              >
                <el-table-column
                  label="类型"
                  width="140"
                >
                  <template #default="{ row }">
                    <el-select
                      v-model="row.itemType"
                      placeholder="请选择类型"
                      size="small"
                      @change="() => onItemTypeChange(row)"
                    >
                      <el-option
                        v-for="type in moneyItemTypes"
                        :key="type"
                        :label="type"
                        :value="type"
                      />
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column
                  label="物品名"
                  width="220"
                >
                  <template #default="{ row }">
                    <el-select
                      v-model="row.itemId"
                      placeholder="请选择物品"
                      size="small"
                      :disabled="!row.itemType"
                      @change="(val) => onItemSelectChange(val, row)"
                    >
                      <el-option
                        v-for="item in moneyItems.filter(x => x.itemType === row.itemType)"
                        :key="item.id"
                        :label="item.itemName"
                        :value="item.id"
                      />
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column
                  label="等级"
                  width="100"
                >
                  <template #default="{ row }">
                    <el-input
                      v-model="row.level"
                      disabled
                      size="small"
                      style="width: 100%"
                    />
                  </template>
                </el-table-column>
                <el-table-column
                  label="个数"
                  width="100"
                >
                  <template #default="{ row }">
                    <el-input-number
                      v-model.number="row.quantity"
                      :min="1"
                      size="small"
                      style="width: 100%"
                    />
                  </template>
                </el-table-column>
                <el-table-column
                  label="金钱 (两)"
                  width="140"
                >
                  <template #default="{ row }">
                    <el-input-number
                      v-model.number="row.money"
                      :min="0"
                      size="small"
                      style="width: 100%"
                    />
                  </template>
                </el-table-column>
                <el-table-column
                  label="操作"
                  width="100"
                >
                  <template #default="{ row }">
                    <el-button
                      type="danger"
                      size="small"
                      @click="removeItemIncome(row)"
                    >
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </el-col>
        </el-row>

        <el-divider>成本信息</el-divider>

        <el-row :gutter="20">
          <el-col
            :xs="24"
            :sm="12"
            :md="6"
          >
            <el-form-item label="点卡 (小时)">
              <el-input
                v-model.number="form.cardCostHours"
                type="number"
                placeholder="请输入点卡消耗时长"
              />
            </el-form-item>
          </el-col>
          <el-col
            :xs="24"
            :sm="12"
            :md="6"
          >
            <el-form-item label="点数/小时 (点)">
              <el-input
                v-model.number="form.cardCostPerHour"
                type="number"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col
            :xs="24"
            :sm="12"
            :md="6"
          >
            <el-form-item label="兑换率 (两/点)">
              <el-input
                v-model.number="form.cardCostRate"
                type="number"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col
            :xs="24"
            :sm="12"
            :md="6"
          >
            <el-form-item label="净收益 (两)">
              <el-input
                v-model.number="form.netIncome"
                type="number"
                disabled
                placeholder="自动计算"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col
            :xs="24"
            :sm="12"
          >
            <el-form-item label="时间 (分钟)">
              <el-input
                v-model.number="form.timeSpent"
                type="number"
                placeholder="请输入花费时间"
              />
            </el-form-item>
          </el-col>
          <el-col
            :xs="24"
            :sm="12"
          >
            <el-form-item label="备注">
              <el-input
                v-model="form.notes"
                placeholder="请输入备注信息"
              />
            </el-form-item>
          </el-col>
        </el-row>

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
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'
import { roundMoney } from '../utils/format'

const loading = ref(false)
const accounts = ref([])
const moneyItems = ref([])
const incomeTypes = ref([])

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

const addBasicIncome = () => {
  form.value.basicIncomeRecords.push({ category: null, incomeId: null, experience: 0, money: 0 })
}

const removeBasicIncome = (row) => {
  const index = form.value.basicIncomeRecords.indexOf(row)
  if (index !== -1) {
    form.value.basicIncomeRecords.splice(index, 1)
  }
}

const onBasicCategoryChange = (row) => {
  row.incomeId = null
}

const addItemIncome = () => {
  form.value.itemIncomeRecords.push({ basicIncomeIndex: null, itemType: null, itemId: null, itemName: '', level: '', quantity: 1, money: 0 })
}

const removeItemIncome = (row) => {
  const index = form.value.itemIncomeRecords.indexOf(row)
  if (index !== -1) {
    form.value.itemIncomeRecords.splice(index, 1)
  }
}

const onItemTypeChange = (row) => {
  row.itemId = null
  row.itemName = ''
  row.level = ''
  row.money = 0
}

const onItemSelectChange = (val, row) => {
  const mi = moneyItems.value.find(m => m.id === val)
  if (mi) {
    row.itemName = mi.itemName
    row.level = mi.level || row.level || ''
    row.money = mi.moneyValue || 0
  } else {
    row.itemName = ''
    row.level = ''
    row.money = 0
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
  itemIncomeRecords: [],
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
  const basic = form.value.basicIncomeRecords.reduce((sum, record) => sum + (record.money || 0), 0)
  const item = form.value.itemIncomeRecords.reduce((sum, record) => sum + ((record.money || 0) * (record.quantity || 1)), 0)
  return basic + item
}

// 监听成本变化，自动计算净收益
watch(
  () => ({
    basicIncomeRecords: form.value.basicIncomeRecords,
    itemIncomeRecords: form.value.itemIncomeRecords,
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
  // 验证必填字段：账号ID及至少一条收益记录
  if (!form.value.accountId || (form.value.basicIncomeRecords.length + form.value.itemIncomeRecords.length) === 0) {
    ElMessage.warning('请选择账号并添加收益记录')
    return
  }

  // 验证并格式化记录日期
  const recordDate = formatDate(form.value.recordDate)
  if (!recordDate) {
    ElMessage.warning('请选择有效的记录日期')
    return
  }

  loading.value = true
  try {
    // 合并基础收益记录和物品收益记录，构建统一的收入记录数组
    const incomeRecords = [
      ...form.value.basicIncomeRecords.map((row) => {
        const typeItem = incomeTypes.value.find(i => i.id === row.incomeId)
        return {
          type: row.category,
          activityType: typeItem?.typeName || '',
          activityTypeId: row.incomeId,
          experience: row.experience,
          money: row.money
        }
      }),
      ...form.value.itemIncomeRecords.map((row) => {
        // 获取关联的基本收益
        const basicIncome = row.basicIncomeIndex !== null ? form.value.basicIncomeRecords[row.basicIncomeIndex] : null
        const typeItem = basicIncome ? incomeTypes.value.find(i => i.id === basicIncome.incomeId) : null
        return {
          type: basicIncome?.category || row.itemType,
          activityType: typeItem?.typeName || '',
          activityTypeId: basicIncome?.incomeId || null,
          itemId: row.itemId,
          itemName: row.itemName,
          level: row.level,
          quantity: row.quantity,
          money: row.money
        }
      })
    ]

    // 组装最终提交的数据对象，包含格式化日期和计算后的总收入
    const raw = {
      ...form.value,
      recordDate,
      totalIncome: calculateTotalIncome(),
      incomeRecords
    }

    // 移除 Vue 响应式代理（Proxy），确保转换为纯 JSON 对象
    const payload = JSON.parse(JSON.stringify(raw))

    console.log('Submitting income record payload:', payload)

    // 查询指定账号和日期下是否已存在收益记录
    const existingRecords = await request.get('/income-records', {
      params: {
        accountId: payload.accountId,
        startDate: recordDate,
        endDate: recordDate
      }
    })

    let response
    // 根据是否存在现有记录决定执行更新（PUT）还是创建（POST）操作
    if (Array.isArray(existingRecords) && existingRecords.length > 0) {
      const existing = existingRecords[0]
      payload.id = existing.id
      console.log('Existing record found, updating recordId=', existing.id)
      response = await request.put(`/income-records/${existing.id}`, payload)
    } else {
      response = await request.post('/income-records', payload)
    }

    console.log('Submit response:', response)
    ElMessage.success('记录保存成功')
    resetForm()
  } catch (error) {
    console.error('保存失败:', error)
    const message = error?.response?.data?.message || error.message || '未知错误'
    ElMessage.error('保存失败: ' + message)
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  const currentCardCostPerHour = form.value.cardCostPerHour
  const currentCardCostRate = form.value.cardCostRate
  form.value = {
    accountId: null,
    recordDate: new Date(),
    basicIncomeRecords: [],
    itemIncomeRecords: [],
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
</style>
