<script lang="ts">
import { defineComponent, ref } from 'vue'
import { NButton, useMessage } from 'naive-ui'
import { getEmailVerify } from '@/api/login'
import ls from '@/utils/ls'

export default defineComponent({
  name: 'EmailVerify',
  components: {
    NButton
  },
  props: {
    username: {
      type: String,
      required: true
    },
    code: {
      type: String,
      required: true
    }
  },
  setup(props) {
    const message = useMessage()
    const isLoading = ref(false)
    const getCode = () => {
      isLoading.value = true
      if (props.username && props.code) {
        getEmailVerify(props.username, ls.getItem('randomID'), props.code)
          .then(
            ({ succeed, res }) => {
              if (succeed) {
                ls.setItem('emailKey', res.result)
                message.success('邮箱验证码发送成功，请检查您的邮箱')
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
                message.error('获取验证码请求发送失败，请检查您的网络')
              } else {
                // 在构建请求时发生的其他错误
                message.error('获取验证码时发生了未知错误')
              }
            }
          )
          .finally(() => {
            isLoading.value = false
          })
      } else {
        if (!props.username) {
          message.warning('请先输入邮箱地址')
        } else if (!props.code) {
          message.warning('请先输入图片验证码')
        }
        isLoading.value = false
      }
    }
    return { isLoading, getCode }
  }
})
</script>

<template>
  <div class="email-verify">
    <NButton secondary :loading="isLoading" @click="getCode">获取邮箱验证码</NButton>
  </div>
</template>

<style lang="scss">
.email-verify {
  width: fit-content;
  margin-left: 8px;

  .n-button {
    width: 8rem;
    font-size: 13px;
    background-repeat: no-repeat;
    background-position: center;
    background-size: cover;
  }
}
</style>
