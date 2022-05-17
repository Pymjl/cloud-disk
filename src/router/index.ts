import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'LoginPage',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/logout',
    name: 'LogoutPage',
    component: () => import('@/views/Logout.vue')
  },
  {
    path: '/register',
    name: 'RegisterPage',
    component: () => import('@/views/Register.vue')
  },
  {
    path: '/files',
    name: 'FilesPage',
    component: () => import('@/views/Files.vue')
  },
  {
    path: '/personal',
    name: 'PersonalPage',
    component: () => import('@/views/Personal.vue')
  },
  {
    path: '/admin',
    name: 'AdminPage',
    component: () => import('@/views/Admin.vue')
  },
  {
    // 将所有未匹配的路由重定向到登录页
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
