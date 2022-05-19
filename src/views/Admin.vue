<script lang="ts">
import { defineComponent, inject, onBeforeMount, Ref } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage, useLoadingBar } from 'naive-ui'
import TopNav from '@/components/public/TopNav.vue'

export default defineComponent({
  name: 'AdminPage',
  components: { TopNav },
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

    const loadingBar = useLoadingBar()
    const router = useRouter()
    const goTo = (to: string) => {
      loadingBar.start()
      router.push(to).then(() => loadingBar.finish())
    }

    onBeforeMount(() => {
      // 拦截一下非管理员用户
      if (userInfo.value.identity !== 1) {
        message.info('您没有权限访问管理面板')
        goTo('/files')
      }
    })
  }
})
</script>

<template>
  <TopNav />
  <p>AdminPage</p>
</template>
