<script lang="ts">
import { defineComponent, onMounted, inject, Ref, Component, h } from 'vue'
import { useRouter } from 'vue-router'
import { Settings, UserProfile, Logout } from '@vicons/carbon'
import { logout } from '@/api/login'
import { getUserInfo } from '@/api/user'
import { useMessage, useLoadingBar, NIcon } from 'naive-ui'

export default defineComponent({
  name: 'TopNav',
  components: { Settings },
  setup() {
    const message = useMessage()
    const userInfo = inject('userInfo') as Ref<{
      id: number
      username: string
      nickname: string
      avatar: string
      identity: number
      createTime: number
      updateTime: number
    }>
    const personalActive = inject('personalActive') as Ref<boolean>
    const adminActive = inject('adminActive') as Ref<boolean>

    onMounted(() => {
      // 每次进入页面都获取用户信息
      getUserInfo().then(
        ({ succeed, res }) => {
          if (succeed) {
            userInfo.value = res.result
          } else {
            message.warning(res.message)
          }
        },
        (err) => {
          if (err.response) {
            // 服务器响应状态码不属于 2xx
            message.error(err.response.data.error)
          } else if (err.request) {
            // 未收到服务器响应
            message.error('登录请求发送失败，请检查您的网络')
          }
        }
      )
    })

    const loadingBar = useLoadingBar()
    const router = useRouter()
    const goTo = (to: string) => {
      loadingBar.start()
      router.push(to).then(() => loadingBar.finish())
    }

    // 渲染图标工具函数
    const renderIcon = (icon: Component) => {
      return () => {
        return h(NIcon, null, {
          default: () => h(icon)
        })
      }
    }

    // 头像下拉菜单内容
    const dropdownOptions = [
      {
        label: '个人信息',
        key: 'personal',
        icon: renderIcon(UserProfile)
      },
      {
        label: '退出登录',
        key: 'logout',
        icon: renderIcon(Logout)
      }
    ]

    // 头像下拉菜单选项动作
    const handleDropdownSelect = (key: string) => {
      switch (key) {
        case 'personal':
          personalActive.value = true
          break
        case 'logout':
          // 通知后端本地已登出，这里不对后端响应做任何处理
          logout()
          // 清空本地存储（Token 等）
          localStorage.clear()
          // 跳转到登录页
          goTo('/login')
          break
        default:
          break
      }
    }

    return {
      userInfo,
      dropdownOptions,
      handleDropdownSelect,
      goTo,
      openAdmin: () => (adminActive.value = true),
      openPersonal: () => (personalActive.value = true)
    }
  }
})
</script>

<template>
  <nav class="top-nav">
    <h1 class="title" @click="goTo('/')">企业云盘</h1>
    <div class="actions">
      <!-- 用户权限值为 1 时显示管理面板入口 -->
      <NButton v-if="userInfo.identity === 1" quaternary circle size="large" @click="openAdmin">
        <template #icon>
          <NIcon><Settings /></NIcon>
        </template>
      </NButton>
      <NDropdown trigger="hover" :options="dropdownOptions" @select="handleDropdownSelect">
        <NAvatar :src="userInfo.avatar" :size="38" round @click="openPersonal" />
      </NDropdown>
    </div>
  </nav>
</template>

<style lang="scss">
.top-nav {
  display: flex;
  align-items: center;
  height: calc(3.5rem - 1px);
  border-bottom: 1px solid #eee;

  .title {
    margin-right: 3rem;
    margin-left: 1.4rem;
    font-size: 1.4rem;
    font-weight: bold;
  }

  .actions {
    display: flex;
    align-items: center;
    margin-right: 1.4rem;
    margin-left: auto;

    :not(:last-child) {
      margin-right: 0.5rem;
    }
  }
}
</style>
