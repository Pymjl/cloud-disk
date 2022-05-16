<template>
  <router-view></router-view>
</template>

<style lang="scss">
// 清除默认边距
* {
  padding: 0;
  margin: 0;
}

body {
  // 保证最小页面宽度为 1024px
  min-width: 1024px;

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
