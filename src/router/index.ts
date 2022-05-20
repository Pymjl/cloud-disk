import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import ls from '@/utils/ls'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'FilesPage',
    meta: {
      title: '企业云盘',
      requireAuth: true
    },
    component: () => import('@/views/Files.vue')
  },
  {
    path: '/login',
    name: 'LoginPage',
    meta: {
      title: '企业云盘 - 登录'
    },
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/register',
    name: 'RegisterPage',
    meta: {
      title: '企业云盘 - 注册'
    },
    component: () => import('@/views/Register.vue')
  },
  {
    // 将所有未匹配的路由重定向到登录页
    path: '/:pathMatch(.*)*',
    redirect: '/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局前置守卫
router.beforeEach((to, from, next) => {
  // 设置标题
  document.title = (to.meta.title as string) ? (to.meta.title as string) : '企业云盘'

  // 拦截需要登录的页面
  if (to.meta.requireAuth) {
    if (ls.getItem('token')) {
      // 有 Token，直接进入需要登录的页面
      next()
    } else {
      // 没有 Token，跳转到登录页
      next('/login')
    }
  } else {
    if (ls.getItem('token')) {
      // 其他不需要 Token 的页面（登录和注册），若有 Token 直接进入文件管理页
      next('/')
    } else {
      // 没有 Token，继续访问
      next()
    }
  }
})

export default router
