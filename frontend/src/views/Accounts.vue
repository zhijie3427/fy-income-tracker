<template>
  <div class="accounts-container">
    <el-row :gutter="20" style="margin-bottom: 20px">
      <div class="card-header" style="justify-content: flex-end;">
        <el-button type="primary" size="small" @click="openAddDialog"
          >新增账号</el-button
        >
      </div>
    </el-row>
    <el-row :gutter="20" style="margin-bottom: 20px">
      <!-- <el-col :xs="24" :sm="16"> -->
        <el-card>
          <template #header>
            <div class="card-header">
              <span>账号列表</span>
            </div>
          </template>

          <el-table :data="paginatedAccounts" stripe style="width: 100%">
            <el-table-column prop="accountName" label="账号名称" width="220" />
            <el-table-column prop="sectName" label="门派" width="160" />
            <el-table-column
              prop="currentExperience"
              label="当前经验"
              width="140"
            />
            <el-table-column
              prop="startingMoney"
              label="所带金钱"
              width="140"
            />
            <el-table-column prop="level" label="等级" width="100" />
            <el-table-column prop="status" label="状态" width="120" />
            <el-table-column prop="notes" label="备注" />
            <el-table-column label="操作" width="220">
              <template #default="{ row }">
                <el-button
                  type="primary"
                  size="small"
                  @click="openEditDialog(row)"
                >
                  编辑
                </el-button>
                <el-button
                  type="danger"
                  size="small"
                  style="margin-left: 8px"
                  @click="deleteAccount(row.id)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="margin-top: 20px; text-align: right;">
            <el-pagination
              background
              layout="prev, pager, next"
              :current-page="accountsPage"
              :page-size="accountsPageSize"
              :total="accounts.length"
              @current-change="(page) => accountsPage = page"
            />
          </div>
        </el-card>
    </el-row>
           <!-- 新增账号对话框 -->
        <el-dialog v-model="showAddDialog" :title="dialogTitle">
          <el-form :model="form" label-width="100px">
            <el-form-item label="账号名称">
              <el-input
                v-model="form.accountName"
                placeholder="请输入账号名称"
              />
            </el-form-item>
            <el-form-item label="门派">
              <el-select v-model="form.sectId" placeholder="请选择门派">
                <el-option
                  v-for="s in factions"
                  :key="s.id"
                  :label="s.name"
                  :value="s.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="等级">
              <el-input
                v-model.number="form.level"
                type="number"
                style="width:100%"
              />
            </el-form-item>
            <el-form-item label="当前经验">
              <el-input
                v-model.number="form.currentExperience"
                type="number"
                placeholder="请输入当前经验"
              />
            </el-form-item>
            <el-form-item label="所带金钱">
              <el-input
                v-model.number="form.startingMoney"
                type="number"
                placeholder="请输入所带金钱"
              />
            </el-form-item>
            <el-form-item label="状态">
              <el-select v-model="form.status" placeholder="请选择状态">
                <el-option label="正常" value="正常" />
                <el-option label="休息" value="休息" />
                <el-option label="暂停" value="暂停" />
              </el-select>
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="form.notes" placeholder="请输入备注" />
            </el-form-item>
          </el-form>
          <template #footer>
            <el-button @click="showAddDialog = false">取消</el-button>
            <el-button type="primary" :loading="loading" @click="saveAccount"
              >保存</el-button
            >
          </template>
        </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'

const accounts = ref([])
const factions = ref([])
const accountsPage = ref(1)
const accountsPageSize = 10
const loading = ref(false)
const showAddDialog = ref(false)
const dialogTitle = ref('新增账号')
const editingAccountId = ref(null)
const form = ref({ accountName: '', sectId: null, level: 1, currentExperience: 0, startingMoney: 0, status: '正常', notes: '' })

const paginatedAccounts = computed(() => {
  const start = (accountsPage.value - 1) * accountsPageSize
  return accounts.value.slice(start, start + accountsPageSize)
})

const loadAccounts = async () => {
  try {
    const list = await request.get('/accounts')
    // 若后端返回 sectId，则用本地门派列表映射名称
    const sectMap = {}
    factions.value.forEach(s => { sectMap[s.id] = s.name })
    accounts.value = list.map(a => ({
      ...a,
      sectName: a.sectName || (a.sectId ? sectMap[a.sectId] : '')
    }))
  } catch (error) {
    console.error('加载账号失败:', error)
  }
}

const loadFactions = async () => {
  try {
    factions.value = await request.get('/factions')
  } catch (error) {
    console.error('加载门派失败:', error)
  }
}

/**
 * 保存账号信息（新增或更新）
 * 
 * 该函数执行以下操作：
 * 1. 验证必填字段（账号名称）
 * 2. 根据是否存在 editingAccountId 判断是新增还是更新操作
 * 3. 调用对应的 API 接口保存数据
 * 4. 成功后关闭对话框、重置表单并刷新账号列表
 * 5. 失败时显示错误消息
 * 
 * @returns {Promise<void>} 无返回值，但为异步函数，可能抛出异常
 */
const saveAccount = async () => {
  // 验证账号名称是否已填写
  if (!form.value.accountName) {
    ElMessage.warning('请输入账号名称')
    return
  }

  loading.value = true
  try {
    // 构建请求 payload，包含所有需要保存的账号字段
    const payload = {
      accountName: form.value.accountName,
      sectId: form.value.sectId,
      level: form.value.level,
      currentExperience: form.value.currentExperience,
      startingMoney: form.value.startingMoney,
      status: form.value.status,
      notes: form.value.notes
    }

    // 根据是否存在编辑中的账号 ID，决定调用更新接口还是新增接口
    if (editingAccountId.value) {
      await request.put(`/accounts/${editingAccountId.value}`, payload)
      ElMessage.success('账号更新成功')
    } else {
      await request.post('/accounts', payload)
      ElMessage.success('账号添加成功')
    }

    // 保存成功后关闭对话框、重置表单并重新加载账号列表
    showAddDialog.value = false
    resetForm()
    await loadAccounts()
  } catch (error) {
    // 捕获异常并显示友好的错误提示
    ElMessage.error('保存失败: ' + (error.response?.data?.message || error.message))
  } finally {
    // 无论成功或失败，均结束加载状态
    loading.value = false
  }
}

const deleteAccount = async (id) => {
  ElMessageBox.confirm('确认删除该账号吗？', '提示', {
    confirmButtonText: '删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/accounts/${id}`)
      ElMessage.success('删除成功')
      await loadAccounts()
    } catch (error) {
      ElMessage.error('删除失败: ' + (error.response?.data?.message || error.message))
    }
  })
}

const openAddDialog = () => {
  dialogTitle.value = '新增账号'
  editingAccountId.value = null
  resetForm()
  showAddDialog.value = true
}

const openEditDialog = (row) => {
  dialogTitle.value = '编辑账号'
  editingAccountId.value = row.id
  form.value = {

    accountName: row.accountName || '',
    sectId: row.sectId || null,
    level: row.level || 1,
    currentExperience: row.currentExperience || 0,
    startingMoney: row.startingMoney || 0,
    status: row.status || '正常',
    notes: row.notes || ''
  }
  showAddDialog.value = true
}

const resetForm = () => {
  form.value = { accountName: '', sectId: null, level: 1, currentExperience: 0, startingMoney: 0, status: '正常', notes: '' }
}

onMounted(async () => {
  await loadFactions()
  await loadAccounts()
})
</script>

<style scoped>
.accounts-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
