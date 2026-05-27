<template>
  <div class="system-maintenance-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>系统维护</span>
        </div>
      </template>

      <el-tabs v-model="activeName">
        <!-- 点卡信息选项卡 -->
        <el-tab-pane label="点卡信息" name="pointCard">
          <div class="tab-content">
            <el-form :model="pointCardForm" label-width="120px">
              <el-form-item label="点数/小时">
                <el-input v-model.number="pointCardForm.pointsPerHour" placeholder="请输入点数/小时" />
              </el-form-item>
              <el-form-item label="兑换率">
                <el-input v-model.number="pointCardForm.exchangeRate" placeholder="请输入兑换率" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="savePointCard">保存</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>

        <!-- 收益维护选项卡 -->
        <el-tab-pane label="收益维护" name="incomeType">
          <div class="tab-content">
            <div class="button-bar">
              <el-button type="primary" @click="addIncomeType">新增</el-button>
            </div>
            <el-table :data="paginatedIncomeTypeList" style="width: 100%; margin-top: 20px">
              <el-table-column prop="id" label="ID" width="80" />
              <el-table-column prop="type" label="类型" />
              <el-table-column prop="typeName" label="收益类型" />
              <el-table-column label="操作" width="120">
                <template #default="{ row }">
                  <el-button link type="primary" @click="editIncomeType(row)">编辑</el-button>
                  <el-button link type="danger" @click="deleteIncomeType(row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div style="margin-top: 20px; text-align: right;">
              <el-pagination
                background
                layout="prev, pager, next"
                :current-page="incomeTypePage"
                :page-size="pageSize"
                :total="incomeTypeList.length"
                @current-change="(page) => incomeTypePage = page"
              />
            </div>

            <!-- 新增/编辑收益类型对话框 -->
            <el-dialog
              v-model="incomeTypeDialogVisible"
              :title="editingIncomeTypeId ? '编辑收益类型' : '新增收益类型'"
              width="400px"
            >
              <el-form :model="incomeTypeForm" label-width="100px">
                <el-form-item label="分类">
                  <el-input v-model="incomeTypeForm.type" placeholder="请输入分类" />
                </el-form-item>
                <el-form-item label="收益类型">
                  <el-input v-model="incomeTypeForm.typeName" placeholder="请输入收益类型" />
                </el-form-item>
              </el-form>
              <template #footer>
                <el-button @click="incomeTypeDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="saveIncomeType">保存</el-button>
              </template>
            </el-dialog>

            <div style="margin-top: 20px">
              <el-button type="primary" @click="saveAllIncomeType">保存全部</el-button>
            </div>
          </div>
        </el-tab-pane>

        <!-- 物品维护选项卡 -->
        <el-tab-pane label="物品维护" name="moneyItem">
          <div class="tab-content">
            <div class="button-bar">
              <el-button type="primary" @click="addMoneyItem">新增</el-button>
            </div>
            <el-table :data="paginatedMoneyItemList" style="width: 100%; margin-top: 20px">
              <el-table-column prop="id" label="ID" width="80" />
              <el-table-column prop="itemType" label="物品类型" />
              <el-table-column prop="itemName" label="物品" />
              <el-table-column prop="moneyValue" label="金钱（两）" width="150">
                <template #default="{ row }">
                  {{ row.moneyValue }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="120">
                <template #default="{ row }">
                  <el-button link type="primary" @click="editMoneyItem(row)">编辑</el-button>
                  <el-button link type="danger" @click="deleteMoneyItem(row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div style="margin-top: 20px; text-align: right;">
              <el-pagination
                background
                layout="prev, pager, next"
                :current-page="moneyItemPage"
                :page-size="pageSize"
                :total="moneyItemList.length"
                @current-change="(page) => moneyItemPage = page"
              />
            </div>

            <!-- 新增/编辑物品对话框 -->
            <el-dialog
              v-model="moneyItemDialogVisible"
              :title="editingMoneyItemId ? '编辑物品' : '新增物品'"
              width="400px"
            >
              <el-form :model="moneyItemForm" label-width="100px">
                <el-form-item label="物品类型">
                  <el-input v-model="moneyItemForm.itemType" placeholder="请输入物品类型" />
                </el-form-item>
                <el-form-item label="物品">
                  <el-input v-model="moneyItemForm.itemName" placeholder="请输入物品名称" />
                </el-form-item>
                <el-form-item label="金钱（两）">
                  <el-input v-model.number="moneyItemForm.moneyValue" placeholder="请输入金钱价值" />
                </el-form-item>
              </el-form>
              <template #footer>
                <el-button @click="moneyItemDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="saveMoneyItem">保存</el-button>
              </template>
            </el-dialog>

            <div style="margin-top: 20px">
              <el-button type="primary" @click="saveAllMoneyItem">保存全部</el-button>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const activeName = ref('pointCard')
const baseURL = 'http://localhost:8080/api/system'

// 点卡信息
const pointCardForm = ref({
  pointsPerHour: null,
  exchangeRate: null
})

// 收益类型
const incomeTypeList = ref([])
const incomeTypeForm = ref({
  type: '',
  typeName: ''
})
const incomeTypeDialogVisible = ref(false)
const editingIncomeTypeId = ref(null)
const incomeTypePage = ref(1)
const pageSize = 10

// 物品维护
const moneyItemList = ref([])
const moneyItemPage = ref(1)
const moneyItemForm = ref({
  itemType: '',
  itemName: '',
  moneyValue: null
})
const moneyItemDialogVisible = ref(false)
const editingMoneyItemId = ref(null)

// 获取点卡信息
const getPointCard = async () => {
  try {
    const response = await axios.get(`${baseURL}/point-card`)
    if (response.data) {
      pointCardForm.value = response.data
    }
  } catch (error) {
    console.error('获取点卡信息失败:', error)
  }
}

const paginatedIncomeTypeList = computed(() => {
  const start = (incomeTypePage.value - 1) * pageSize
  return incomeTypeList.value.slice(start, start + pageSize)
})

const paginatedMoneyItemList = computed(() => {
  const start = (moneyItemPage.value - 1) * pageSize
  return moneyItemList.value.slice(start, start + pageSize)
})

// 保存点卡信息
const savePointCard = async () => {
  try {
     await axios.post(`${baseURL}/point-card`, pointCardForm.value)
    ElMessage.success('点卡信息保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
    console.error('保存失败:', error)
  }
}

// 获取收益类型列表
const getIncomeTypes = async () => {
  try {
    const response = await axios.get(`${baseURL}/income-types`)
    incomeTypeList.value = response.data || []
  } catch (error) {
    console.error('获取收益类型失败:', error)
  }
}

// 新增收益类型
const addIncomeType = () => {
  incomeTypeForm.value = {
    type: '',
    typeName: ''
  }
  editingIncomeTypeId.value = null
  incomeTypeDialogVisible.value = true
}

// 编辑收益类型
const editIncomeType = (row) => {
  incomeTypeForm.value = { ...row }
  editingIncomeTypeId.value = row.id
  incomeTypeDialogVisible.value = true
}

// 删除收益类型
const deleteIncomeType = async (id) => {
  try {
    await axios.delete(`${baseURL}/income-types/${id}`)
    getIncomeTypes()
    ElMessage.success('删除成功')
  } catch (error) {
    ElMessage.error('删除失败')
    console.error('删除失败:', error)
  }
}

// 保存单个收益类型
const saveIncomeType = async () => {
  try {
    if (editingIncomeTypeId.value) {
      await axios.put(`${baseURL}/income-types/${editingIncomeTypeId.value}`, incomeTypeForm.value)
    } else {
      await axios.post(`${baseURL}/income-types`, incomeTypeForm.value)
    }
    incomeTypeDialogVisible.value = false
    getIncomeTypes()
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
    console.error('保存失败:', error)
  }
}

// 保存全部收益类型
const saveAllIncomeType = async () => {
  try {
    await axios.post(`${baseURL}/income-types/batch`, incomeTypeList.value)
    ElMessage.success('全部保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
    console.error('保存失败:', error)
  }
}

// 获取物品列表
const getMoneyItems = async () => {
  try {
    const response = await axios.get(`${baseURL}/money-items`)
    moneyItemList.value = response.data || []
  } catch (error) {
    console.error('获取物品列表失败:', error)
  }
}

// 新增物品
const addMoneyItem = () => {
  moneyItemForm.value = {
    itemType: '',
    itemName: '',
    moneyValue: null
  }
  editingMoneyItemId.value = null
  moneyItemDialogVisible.value = true
}

// 编辑物品
const editMoneyItem = (row) => {
  moneyItemForm.value = { ...row }
  editingMoneyItemId.value = row.id
  moneyItemDialogVisible.value = true
}

// 删除物品
const deleteMoneyItem = async (id) => {
  try {
    await axios.delete(`${baseURL}/money-items/${id}`)
    getMoneyItems()
    ElMessage.success('删除成功')
  } catch (error) {
    ElMessage.error('删除失败')
    console.error('删除失败:', error)
  }
}

// 保存单个物品
const saveMoneyItem = async () => {
  try {
    if (editingMoneyItemId.value) {
      await axios.put(`${baseURL}/money-items/${editingMoneyItemId.value}`, moneyItemForm.value)
    } else {
      await axios.post(`${baseURL}/money-items`, moneyItemForm.value)
    }
    moneyItemDialogVisible.value = false
    getMoneyItems()
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
    console.error('保存失败:', error)
  }
}

// 保存全部物品
const saveAllMoneyItem = async () => {
  try {
    await axios.post(`${baseURL}/money-items/batch`, moneyItemList.value)
    ElMessage.success('全部保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
    console.error('保存失败:', error)
  }
}

// 页面加载时获取数据
onMounted(() => {
  getPointCard()
  getIncomeTypes()
  getMoneyItems()
})
</script>

<style scoped>
.system-maintenance-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tab-content {
  padding: 20px;
}

.button-bar {
  margin-bottom: 20px;
}
</style>
