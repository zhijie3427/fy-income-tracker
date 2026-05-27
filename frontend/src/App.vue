<template>
  <div class="app-layout">
    <header class="header">
      <div class="header-left">
        <h1>收益记录系统</h1>
      </div>
      <div class="header-right">
        <span class="welcome-text">五开玩家</span>
      </div>
    </header>

    <div class="layout-container">
      <aside class="aside">
        <el-menu
          router
          :default-active="$route.path"
          :default-openeds="['/record']"
          class="menu-body"
          @select="handleSelect"
        >
          <el-menu-item index="/dashboard">
            <el-icon><House /></el-icon>
            <span>仪表盘</span>
          </el-menu-item>
          <el-sub-menu index="/record">
            <template #title>
              <el-icon><Edit /></el-icon>
              <span>记录收益</span>
            </template>
            <el-menu-item index="/record/summary">收益汇总</el-menu-item>
            <el-menu-item index="/record/list">收益记录</el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/accounts">
            <el-icon><Ticket /></el-icon>
            <span>账号管理</span>
          </el-menu-item>
          <el-menu-item index="/items">
            <el-icon><Ticket /></el-icon>
            <span>物品维护</span>
          </el-menu-item>
          <el-menu-item index="/statistics">
            <el-icon><DataAnalysis /></el-icon>
            <span>数据统计</span>
          </el-menu-item>
          <el-menu-item index="/system">
            <el-icon><Setting /></el-icon>
            <span>系统维护</span>
          </el-menu-item>
        </el-menu>
      </aside>

      <main class="main">
        <router-view v-slot="{ Component }">
          <component :is="Component" />
        </router-view>
      </main>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { House, Edit, DataAnalysis, Ticket, Setting } from '@element-plus/icons-vue'

const router = useRouter()
const handleSelect = (key) => {
  router.push(key)
}
</script>

<style scoped>
.app-layout {
  min-height: 100vh;
  background: #f5f7fa;
}

.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  background: #fff;
  border-bottom: 1px solid #ebeef5;
  z-index: 10;
}

.header-left h1 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

.header-right {
  display: flex;
  align-items: center;
}

.layout-container {
  display: flex;
  margin-top: 64px;
  min-height: calc(100vh - 64px);
}

.aside {
  position: fixed;
  top: 64px;
  left: 0;
  bottom: 0;
  width: 200px;
  background: #fff;
  border-right: 1px solid #ebeef5;
  overflow-y: auto;
}

.menu-body {
  border: none;
}

.main {
  margin-left: 200px;
  width: calc(100% - 200px);
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 64px);
}
</style>
