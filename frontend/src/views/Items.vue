<template>
  <div class="items-container">
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
              <span>物品价格维护</span>
            </div>
          </template>

          <el-table
            :data="paginatedItems"
            stripe
            style="width: 100%"
          >
            <el-table-column
              prop="name"
              label="物品名称"
              width="200"
            />
            <el-table-column
              prop="level"
              label="等级"
              width="100"
            />
            <el-table-column
              prop="price"
              label="当前价格"
              width="140"
            />
            <el-table-column
              label="操作"
              width="180"
            >
              <template #default="{ row }">
                <el-button
                  type="primary"
                  size="small"
                  @click="editItem(row)"
                >
                  编辑
                </el-button>
                <el-button
                  type="danger"
                  size="small"
                  @click="deleteItem(row)"
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
              :current-page="itemsPage"
              :page-size="itemsPageSize"
              :total="items.length"
              @current-change="(page) => itemsPage = page"
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
              <span>{{ editing ? '编辑物品' : '新增物品' }}</span>
            </div>
          </template>

          <el-form
            :model="form"
            label-width="80px"
          >
            <el-form-item label="物品名称">
              <el-input
                v-model="form.name"
                placeholder="请输入物品名称"
              />
            </el-form-item>
            <el-form-item label="物品等级">
              <el-input-number
                v-model="form.level"
                :min="1"
                :max="150"
                placeholder="请输入物品等级"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="物品价格 (两)">
              <el-input-number
                v-model="form.price"
                :precision="0"
                :min="0"
                placeholder="请输入价格"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                :loading="loading"
                @click="saveItem"
              >
                保存
              </el-button>
              <el-button @click="resetForm">
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'

const items = ref([])
const itemsPage = ref(1)
const itemsPageSize = 10
const form = ref({ id: null, name: '', level: 1, price: 0 })
const editing = ref(false)
const loading = ref(false)

const paginatedItems = computed(() => {
  const start = (itemsPage.value - 1) * itemsPageSize
  return items.value.slice(start, start + itemsPageSize)
})

const loadItems = async () => {
  try {
    items.value = await request.get('/items')
  } catch (error) {
    console.error('加载物品失败:', error)
  }
}

const resetForm = () => {
  form.value = { id: null, name: '', level: 1, price: 0 }
  editing.value = false
}

const saveItem = async () => {
  if (!form.value.name || form.value.price == null) {
    ElMessage.warning('请填写物品名称和价格')
    return
  }

  loading.value = true
  try {
    if (editing.value) {
      await request.put(`/items/${form.value.id}`, form.value)
      ElMessage.success('物品更新成功')
    } else {
      await request.post('/items', form.value)
      ElMessage.success('物品新增成功')
    }
    await loadItems()
    resetForm()
  } catch (error) {
    ElMessage.error('保存失败: ' + (error.response?.data?.message || error.message))
  } finally {
    loading.value = false
  }
}

const editItem = (row) => {
  editing.value = true
  form.value = { id: row.id, name: row.name, level: row.level, price: row.price }
}

const deleteItem = (row) => {
  ElMessageBox.confirm(`确认删除《${row.name}》吗？`, '提示', {
    confirmButtonText: '删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/items/${row.id}`)
      ElMessage.success('删除成功')
      await loadItems()
    } catch (error) {
      ElMessage.error('删除失败: ' + (error.response?.data?.message || error.message))
    }
  })
}

onMounted(() => {
  loadItems()
})
</script>

<style scoped>
.items-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
