import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { requiresGuest: true } // 需要未登录状态
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../views/Layout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('../views/Home.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'staff',
        name: 'Staff',
        component: () => import('../views/staff/StaffManagement.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'customer',
        name: 'Customer',
        component: () => import('../views/customer/CustomerManagement.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'order',
        name: 'Order',
        component: () => import('../views/order/OrderManagement.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'evaluation',
        name: 'Evaluation',
        component: () => import('../views/evaluation/EvaluationManagement.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'complaint',
        name: 'Complaint',
        component: () => import('../views/complaint/ComplaintManagement.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'finance',
        name: 'Finance',
        component: () => import('../views/finance/FinanceManagement.vue'),
        meta: { requiresAuth: true }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !token) {
    // 需要登录但未登录
    next('/login')
  } else if (to.meta.requiresGuest && token) {
    // 需要未登录状态但已登录
    next('/')
  } else {
    next()
  }
})

export default router