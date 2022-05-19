<script lang="ts">
import { defineComponent, onMounted, inject, Ref } from 'vue'
import { Settings } from '@vicons/carbon'
import { getUserInfo } from '@/api/user'
import { useMessage } from 'naive-ui'

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

    return { userInfo }
  }
})
</script>

<template>
  <nav class="top-nav">
    <h1 class="title">企业云盘</h1>
    <div class="actions">
      <NIcon size="22.4"><Settings /></NIcon>
      <NAvatar :src="userInfo.avatar" :size="38" round />
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
  }
}
</style>
