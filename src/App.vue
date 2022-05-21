<script lang="ts">
import { defineComponent, ref } from 'vue'

export default defineComponent({
  name: 'App',
  // 注册了一些全局状态
  provide: {
    randomId: ref(''),
    userInfo: ref({
      id: 0,
      username: '',
      nickname: '',
      avatar: '',
      identity: 0,
      createTime: 0,
      updateTime: 0
    }),
    personalActive: ref(false),
    adminActive: ref(false)
  }
})
</script>

<template>
  <NMessageProvider>
    <NDialogProvider>
      <NLoadingBarProvider>
        <router-view></router-view>
      </NLoadingBarProvider>
    </NDialogProvider>
  </NMessageProvider>
</template>

<style lang="scss">
// 清除默认边距
* {
  padding: 0;
  margin: 0;
}

#app {
  // 保证最小页面宽度为 60rem
  min-width: 60rem;

  // 保证中文字体显示效果
  font-family: -apple-system, 'Noto Sans', 'Helvetica Neue', Helvetica, 'Nimbus Sans L', Arial, 'Liberation Sans', 'PingFang SC',
    'Hiragino Sans GB', 'Noto Sans CJK SC', 'Source Han Sans SC', 'Source Han Sans CN', 'Microsoft YaHei', 'Wenquanyi Micro Hei',
    'WenQuanYi Zen Hei', 'ST Heiti', SimHei, 'WenQuanYi Zen Hei Sharp', sans-serif;
  font-size: 16px;
  -webkit-font-smoothing: antialiased;
  color: #1f2225;

  // 默认禁止用户选择
  -webkit-user-select: none;
  user-select: none;
}
</style>
