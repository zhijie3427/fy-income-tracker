<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h1>收益记录系统</h1>
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <!-- 登录标签页 -->
        <el-tab-pane
          label="登录"
          name="login"
        >
          <el-form
            :model="loginForm"
            label-width="80px"
            @submit.prevent="handleLogin"
          >
            <el-form-item label="用户名">
              <el-input
                v-model="loginForm.username"
                placeholder="请输入用户名"
              />
            </el-form-item>
            <el-form-item label="密码">
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                :loading="loginLoading"
                @click="handleLogin"
              >
                登录
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 注册标签页 -->
        <el-tab-pane
          label="注册"
          name="register"
        >
          <el-form
            :model="registerForm"
            label-width="80px"
            @submit.prevent="handleRegister"
          >
            <el-form-item label="用户名">
              <el-input
                v-model="registerForm.username"
                placeholder="请输入用户名"
              />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input
                v-model="registerForm.email"
                type="email"
                placeholder="请输入邮箱"
              />
            </el-form-item>
            <el-form-item label="密码">
              <el-input
                v-model="registerForm.password"
                type="password"
                placeholder="请输入密码"
              />
            </el-form-item>
            <el-form-item label="手机">
              <el-input
                v-model="registerForm.phone"
                placeholder="请输入手机号（可选）"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                :loading="registerLoading"
                @click="handleRegister"
              >
                注册
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const activeTab = ref('login')
const loginLoading = ref(false)
const registerLoading = ref(false)

const loginForm = ref({
  username: '',
  password: ''
})

const registerForm = ref({
  username: '',
  email: '',
  password: '',
  phone: ''
})

const handleLogin = async () => {
  if (!loginForm.value.username || !loginForm.value.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }

  loginLoading.value = true
  try {
    await userStore.login(loginForm.value.username, loginForm.value.password)
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } catch (error) {
    ElMessage.error('登录失败: ' + (error.response?.data?.message || error.message))
  } finally {
    loginLoading.value = false
  }
}

const handleRegister = async () => {
  if (!registerForm.value.username || !registerForm.value.email || !registerForm.value.password) {
    ElMessage.warning('请填写必要信息')
    return
  }

  registerLoading.value = true
  try {
    await userStore.register(
      registerForm.value.username,
      registerForm.value.password,
      registerForm.value.email,
      registerForm.value.phone
    )
    ElMessage.success('注册成功，请登录')
    activeTab.value = 'login'
  } catch (error) {
    ElMessage.error('注册失败: ' + (error.response?.data?.message || error.message))
  } finally {
    registerLoading.value = false
  }
}
</script>

<style scoped>
.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  text-align: center;
}

.card-header h1 {
  color: #333;
  font-size: 24px;
  margin: 0;
}
</style>
