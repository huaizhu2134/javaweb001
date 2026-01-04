<template>
  <div class="admin-layout">
    <!-- 顶部导航栏 -->
    <header class="admin-header">
      <div class="header-content">
        <div class="header-left">
          <div class="logo">
            <el-icon><User /></el-icon>
            <h1>游戏陪玩管理系统</h1>
          </div>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand" placement="bottom-end">
            <div class="user-info">
              <el-avatar size="small" :icon="UserFilled" class="user-avatar" />
              <span class="user-name">管理员</span>
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  个人资料
                </el-dropdown-item>
                <el-dropdown-item command="logout" divided>
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </header>

    <div class="admin-container">
      <!-- 侧边栏 -->
      <aside :class="['admin-aside', { 'collapsed': isCollapse }]">
        <div class="sidebar-content">
          <el-menu
            :default-active="$route.path"
            class="sidebar-menu"
            :router="true"
            :collapse="isCollapse"
            :collapse-transition="false"
          >
            <el-menu-item index="/">
              <el-icon><House /></el-icon>
              <template #title>首页</template>
            </el-menu-item>
            <el-menu-item index="/staff">
              <el-icon><User /></el-icon>
              <template #title>陪玩管理</template>
            </el-menu-item>
            <el-menu-item index="/customer">
              <el-icon><UserFilled /></el-icon>
              <template #title>客户管理</template>
            </el-menu-item>
            <el-menu-item index="/order">
              <el-icon><Document /></el-icon>
              <template #title>订单管理</template>
            </el-menu-item>
            <el-menu-item index="/evaluation">
              <el-icon><ChatLineRound /></el-icon>
              <template #title>评价管理</template>
            </el-menu-item>
            <el-menu-item index="/complaint">
              <el-icon><ChatLineRound /></el-icon>
              <template #title>投诉管理</template>
            </el-menu-item>
            <el-menu-item index="/finance">
              <el-icon><Money /></el-icon>
              <template #title>财务管理</template>
            </el-menu-item>
          </el-menu>
        </div>
        <div class="sidebar-toggle" @click="toggleSidebar">
          <el-icon><Expand v-if="isCollapse" /><Fold v-else /></el-icon>
        </div>
      </aside>

      <!-- 主内容区域 -->
      <main class="admin-main">
        <div class="admin-content">
          <router-view v-slot="{ Component }">
            <keep-alive>
              <component :is="Component" v-if="$route.meta.keepAlive" />
            </keep-alive>
            <component :is="Component" v-if="!$route.meta.keepAlive" />
          </router-view>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import {
  House,
  User,
  UserFilled,
  Document,
  ChatLineRound,
  Money,
  ArrowDown,
  SwitchButton,
  Expand,
  Fold
} from '@element-plus/icons-vue'

import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const isCollapse = ref(false)

const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value
}

const handleCommand = async (command) => {
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm(
        '确定要退出登录吗？',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
      )
      // 清除登录信息
      localStorage.removeItem('token')
      ElMessage.success('已退出登录')
      // 跳转到登录页
      router.push('/login')
    } catch {
      // 取消操作
    }
  } else if (command === 'profile') {
    // 跳转到个人资料页
    ElMessage.info('个人资料功能开发中...')
  }
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  background-color: var(--bg-color-page);
}

.admin-container {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.admin-header {
  height: var(--header-height);
  background: var(--bg-color-header);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 var(--spacing-lg);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  z-index: 1000;
  flex-shrink: 0;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.header-left .logo {
  display: flex;
  align-items: center;
  color: white;
}

.header-left .logo .el-icon {
  font-size: 24px;
  margin-right: 10px;
}

.header-left .logo h1 {
  margin: 0;
  font-size: 18px;
  font-weight: var(--font-weight-semibold);
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background-color 0.3s;
  color: white;
}

.user-info:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.user-avatar {
  margin-right: 8px;
}

.user-name {
  margin-right: 4px;
  font-weight: var(--font-weight-medium);
}

.admin-aside {
  width: var(--sidebar-width);
  background-color: var(--bg-color-sidebar);
  transition: all 0.3s;
  overflow-x: hidden;
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
}

.admin-aside.collapsed {
  width: var(--sidebar-collapsed-width);
}

.sidebar-content {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  padding: var(--spacing-sm) 0;
}

.sidebar-menu {
  border-right: none;
  height: 100%;
  overflow-y: auto;
}

.sidebar-menu :deep(.el-menu-item) {
  margin: 4px 8px;
  border-radius: 4px;
  height: 44px;
  line-height: 44px;
  color: rgba(255, 255, 255, 0.7);
}

.sidebar-menu :deep(.el-menu-item.is-active) {
  background-color: rgba(255, 255, 255, 0.1);
  color: #fff;
  border-left: 3px solid var(--primary-color);
}

.sidebar-menu :deep(.el-menu-item:hover) {
  background-color: rgba(255, 255, 255, 0.2);
}

.sidebar-toggle {
  height: 44px;
  line-height: 44px;
  text-align: center;
  cursor: pointer;
  color: rgba(255, 255, 255, 0.7);
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 8px;
  border-radius: 4px;
}

.sidebar-toggle:hover {
  background-color: rgba(255, 255, 255, 0.2);
  color: #fff;
}

.admin-main {
  flex: 1;
  overflow: auto;
  padding: var(--spacing-md);
  background-color: var(--bg-color-page);
}

.admin-content {
  background: var(--color-white);
  border-radius: var(--radius-md);
  padding: var(--spacing-lg);
  box-shadow: var(--shadow-md);
  min-height: calc(100% - 40px);
  height: 100%;
}

/* 滚动条样式 */
:deep(.el-main::-webkit-scrollbar) {
  width: 6px;
}

:deep(.el-main::-webkit-scrollbar-track) {
  background: #f1f1f1;
}

:deep(.el-main::-webkit-scrollbar-thumb) {
  background: #c1c1c1;
  border-radius: 3px;
}

:deep(.el-main::-webkit-scrollbar-thumb:hover) {
  background: #a8a8a8;
}

:deep(.el-aside::-webkit-scrollbar) {
  width: 0;
}

:deep(.el-menu) {
  background-color: transparent;
  border-right: none;
}

:deep(.el-menu-item) {
  color: rgba(255, 255, 255, 0.7);
}

:deep(.el-menu-item:hover) {
  color: #fff;
}
</style>