<template>
  <div class="accounts-container">
    <el-row
      :gutter="20"
      style="margin-bottom: 20px"
    >
      <el-col
        :xs="24"
        :sm="16"
      >
        <el-card>
          <template #header>
            <div class="card-header">
              <span>账号列表</span>
            </div>
          </template>

          <el-table
            :data="paginatedAccounts"
            stripe
            style="width: 100%"
          >
            <el-table-column
              prop="accountName"
              label="账号名称"
              width="220"
            />
            <el-table-column
              prop="accountIndex"
              label="账号序号"
              width="120"
            />
            <el-table-column
              prop="level"
              label="等级"
              width="100"
            />
            <el-table-column
              prop="status"
              label="状态"
              width="120"
            />
            <el-table-column
              prop="notes"
              label="备注"
            />
            <el-table-column
              label="操作"
              width="180"
            >
              <template #default="{ row }">
                <el-button
                  type="danger"
                  size="small"
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
      </el-col>

      <el-col
        :xs="24"
        :sm="8"
      >
        <el-card>
          <template #header>
            <div class="card-header">
              <span>账号管理</span>
              <el-button type="primary" size="small" @click="showAddDialog = true">新增账号</el-button>
            </div>
          </template>

          <!-- 新增账号对话框 -->
          <el-dialog v-model="showAddDialog" title="新增账号">
            <el-form :model="form" label-width="100px">
              <el-form-item label="账号名称">
                <el-input v-model="form.accountName" placeholder="请输入账号名称" />
              </el-form-item>
              <el-form-item label="账号序号">
                <el-input-number v-model="form.accountIndex" :min="1" :max="10" style="width:100%" />
              </el-form-item>
              <el-form-item label="等级">
                <el-input-number v-model="form.level" :min="1" :max="150" style="width:100%" />
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
              <el-button type="primary" :loading="loading" @click="saveAccount">保存</el-button>
            </template>
          </el-dialog>

        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'

const accounts = ref([])
const accountsPage = ref(1)
const accountsPageSize = 10
const loading = ref(false)
const showAddDialog = ref(false)
const form = ref({ accountName: '', accountIndex: 1, level: 1, status: '正常', notes: '' })

const paginatedAccounts = computed(() => {
  const start = (accountsPage.value - 1) * accountsPageSize
  return accounts.value.slice(start, start + accountsPageSize)
})

const loadAccounts = async () => {
  try {
    accounts.value = await request.get('/accounts')
  } catch (error) {
    console.error('加载账号失败:', error)
  }
}

const saveAccount = async () => {
  if (!form.value.accountName) {
    ElMessage.warning('请输入账号名称')
    return
  }

  loading.value = true
  try {
    await request.post('/accounts', {
      ...form.value
    })
    ElMessage.success('账号添加成功')
    resetForm()
    await loadAccounts()
  } catch (error) {
    ElMessage.error('保存失败: ' + (error.response?.data?.message || error.message))
  } finally {
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

const resetForm = () => {
  form.value = { accountName: '', accountIndex: 1, level: 1, status: '正常', notes: '' }
}

onMounted(() => {
  loadAccounts()
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
