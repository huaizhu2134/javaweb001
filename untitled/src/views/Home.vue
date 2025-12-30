<template>
  <div class="home-container">
    <!-- 欢迎卡片 -->
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span class="title">系统概览</span>
        </div>
      </template>
      <div class="welcome-content">
        <div class="system-info">
          <p class="subtitle">欢迎使用游戏陪玩后台管理系统</p>
          <p class="description"></p>
        </div>
        <div class="features">
          <h4>系统功能包括：</h4>
          <el-row :gutter="20">
            <el-col :span="12">
              <ul class="feature-list">
                <li>陪玩人员管理：陪玩人员信息录入、查询、修改、状态管理等</li>
                <li>客户管理：客户信息管理、消费统计、等级管理等</li>
                <li>订单管理：订单创建、状态管理、收入统计等</li>
              </ul>
            </el-col>
            <el-col :span="12">
              <ul class="feature-list">
                <li>评价投诉管理：评价管理、投诉处理等</li>
                <li>财务管理：提现管理、收入统计等</li>
                <li>系统管理：管理员、角色、数据字典等</li>
              </ul>
            </el-col>
          </el-row>
        </div>
      </div>
    </el-card>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="statistics-card" shadow="hover">
          <div class="statistics-item">
            <div class="icon-container">
              <el-icon class="statistics-icon"><User /></el-icon>
            </div>
            <div class="statistics-content">
              <div class="statistics-value">{{ statistics.staffCount }}</div>
              <div class="statistics-label">陪玩人员</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card" shadow="hover">
          <div class="statistics-item">
            <div class="icon-container">
              <el-icon class="statistics-icon"><UserFilled /></el-icon>
            </div>
            <div class="statistics-content">
              <div class="statistics-value">{{ statistics.customerCount }}</div>
              <div class="statistics-label">客户</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card" shadow="hover">
          <div class="statistics-item">
            <div class="icon-container">
              <el-icon class="statistics-icon"><Document /></el-icon>
            </div>
            <div class="statistics-content">
              <div class="statistics-value">{{ statistics.orderCount }}</div>
              <div class="statistics-label">订单</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card" shadow="hover">
          <div class="statistics-item">
            <div class="icon-container">
              <el-icon class="statistics-icon"><Money /></el-icon>
            </div>
            <div class="statistics-content">
              <div class="statistics-value">¥{{ statistics.income }}</div>
              <div class="statistics-label">今日收入</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快捷操作 -->
    <el-card class="quick-actions-card">
      <template #header>
        <div class="card-header">
          <span class="title">快捷操作</span>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :span="4" v-for="action in quickActions" :key="action.name">
          <div class="action-item" @click="goToPage(action.path)">
            <div class="action-icon">
              <el-icon :size="24" :class="action.iconClass"><component :is="action.icon" /></el-icon>
            </div>
            <div class="action-text">{{ action.name }}</div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import {
  User,
  UserFilled,
  Document,
  Money,
  Plus,
  Edit,
  Search,
  Setting
} from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()

// 统计数据
const statistics = reactive({
  staffCount: 0,
  customerCount: 0,
  orderCount: 0,
  income: '0'
})

// 获取统计数据
const fetchStatistics = async () => {
  try {
    const response = await request({ url: '/api/test/statistics', method: 'get' })
    if (response.code === 200) {
      statistics.staffCount = response.data.staffCount || 0
      statistics.customerCount = response.data.customerCount || 0
      statistics.orderCount = response.data.orderCount || 0
      statistics.income = (response.data.totalIncome || 0).toLocaleString()
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

// 快捷操作
const quickActions = [
  { name: '添加陪玩', path: '/staff', icon: Plus, iconClass: 'add-icon' },
  { name: '客户管理', path: '/customer', icon: UserFilled, iconClass: 'customer-icon' },
  { name: '创建订单', path: '/order', icon: Plus, iconClass: 'add-icon' },
  { name: '订单查询', path: '/order', icon: Search, iconClass: 'search-icon' },
  { name: '财务管理', path: '/finance', icon: Money, iconClass: 'finance-icon' },
  { name: '系统设置', path: '/system', icon: Setting, iconClass: 'setting-icon' }
]

const goToPage = (path) => {
  router.push(path)
}

onMounted(() => {
  fetchStatistics()
})
</script>

<style scoped>
.home-container {
  padding: var(--spacing-md);
  background-color: var(--bg-color-page);
  min-height: 100%;
}

.welcome-card {
  margin-bottom: var(--spacing-md);
  border: none;
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-md);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--text-color-primary);
}

.welcome-content {
  padding: var(--spacing-sm) 0;
}

.system-info {
  margin-bottom: var(--spacing-md);
}

.subtitle {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-semibold);
  color: var(--text-color-primary);
  margin: 0 0 var(--spacing-sm) 0;
}

.description {
  color: var(--text-color-secondary);
  margin: 0 0 var(--spacing-md) 0;
  line-height: 1.6;
}

.features h4 {
  margin: 0 0 var(--spacing-md) 0;
  color: var(--text-color-primary);
  font-weight: var(--font-weight-semibold);
}

.feature-list {
  padding-left: var(--spacing-lg);
  margin: 0;
}

.feature-list li {
  margin: var(--spacing-xs) 0;
  color: var(--text-color-secondary);
  line-height: 1.5;
}

.statistics-row {
  margin-bottom: var(--spacing-md);
}

.statistics-card {
  height: 100px;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  border: none;
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-sm);
}

.statistics-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
}

.statistics-item {
  display: flex;
  align-items: center;
  height: 100%;
  padding: var(--spacing-sm);
}

.icon-container {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary-color), var(--primary-color-dark));
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: var(--spacing-md);
}

.statistics-icon {
  color: white;
  font-size: 24px;
}

.statistics-content {
  flex: 1;
}

.statistics-value {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  color: var(--text-color-primary);
  margin-bottom: 4px;
}

.statistics-label {
  font-size: var(--font-size-sm);
  color: var(--text-color-placeholder);
}

.quick-actions-card {
  margin-bottom: var(--spacing-md);
  border: none;
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-md);
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: var(--spacing-lg) var(--spacing-sm);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: background-color 0.3s;
  text-align: center;
}

.action-item:hover {
  background-color: var(--bg-color-hover);
}

.action-icon {
  width: 50px;
  height: 50px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: var(--spacing-sm);
  background-color: var(--color-gray-2);
}

.add-icon {
  color: var(--secondary-color);
  background-color: #f0f9ff !important;
}

.customer-icon {
  color: var(--primary-color);
  background-color: #ecf5ff !important;
}

.search-icon {
  color: var(--warning-color);
  background-color: #fdf6ec !important;
}

.finance-icon {
  color: var(--error-color);
  background-color: #fef0f0 !important;
}

.setting-icon {
  color: var(--text-color-placeholder);
  background-color: #f4f4f5 !important;
}

.action-text {
  font-size: var(--font-size-base);
  color: var(--text-color-secondary);
}

:deep(.el-card__header) {
  padding: var(--spacing-md) var(--spacing-lg);
  border-bottom: 1px solid var(--border-color-light);
}

:deep(.el-card__body) {
  padding: var(--spacing-lg);
}
</style>