<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useRouter } from 'vue-router'
import { FormInst, useLoadingBar, useMessage } from 'naive-ui'
import BgWrapper from '@/components/login/BgWrapper.vue'
import ImageVerify from '@/components/login/ImageVerify.vue'
import EmailVerify from '@/components/login/EmailVerify.vue'
import { login } from '@/api/login'
import ls from '@/utils/ls'

export default defineComponent({
  name: 'LoginPage',
  components: { BgWrapper, ImageVerify, EmailVerify },
  setup() {
    const router = useRouter()
    const message = useMessage()
    const loadingBar = useLoadingBar()
    const goTo = (to: string) => {
      loadingBar.start()
      router.push(to).then(() => loadingBar.finish())
    }

    // 表单验证
    const formRef = ref<FormInst | null>(null)
    const formValue = ref({
      username: '',
      password: '',
      imageVerify: '',
      emailVerify: ''
    })
    const rules = {
      username: {
        required: true,
        message: '请输入邮箱地址',
        trigger: 'input'
      },
      password: {
        required: true,
        message: '请输入密码',
        trigger: 'input'
      },
      imageVerify: {
        required: true,
        message: '请输入图片验证码',
        trigger: 'input'
      },
      emailVerify: {
        required: true,
        message: '请输入邮箱验证码',
        trigger: 'input'
      }
    }
    const handleValidateClick = (e: MouseEvent) => {
      e.preventDefault()
      formRef.value?.validate((errors) => {
        if (!errors) {
          // 表单验证通过即可登录
          login(formValue.value.username, formValue.value.password, formValue.value.emailVerify, ls.getItem('emailKey')).then(
            ({ succeed, res }) => {
              if (succeed) {
                ls.setItem('token', res.result)
                message.success('登录成功，即将跳转到首页')
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
        }
      })
    }

    return {
      formRef,
      formValue,
      rules,
      goTo,
      handleValidateClick
    }
  }
})
</script>

<template>
  <BgWrapper>
    <div class="login-card">
      <div class="header">
        <h1>登录</h1>
      </div>
      <div class="main">
        <NForm ref="formRef" :show-label="false" :model="formValue" :rules="rules">
          <NFormItem path="username">
            <NInput v-model:value="formValue.username" placeholder="邮箱地址" autofocus />
          </NFormItem>
          <NFormItem path="password">
            <NInput v-model:value="formValue.password" type="password" show-password-on="mousedown" placeholder="密码" />
          </NFormItem>
          <NFormItem path="imageVerify">
            <NInput v-model:value="formValue.imageVerify" placeholder="图片验证码" />
            <ImageVerify />
          </NFormItem>
          <NFormItem path="emailVerify">
            <NInput v-model:value="formValue.emailVerify" placeholder="邮箱验证码" />
            <EmailVerify :username="formValue.username" :code="formValue.imageVerify" />
          </NFormItem>
        </NForm>
        <div class="footer">
          <NButton secondary type="info" @click="goTo('/register')">新建账号</NButton>
          <NButton type="primary" @click="handleValidateClick">登录</NButton>
        </div>
      </div>
    </div>
  </BgWrapper>
</template>

<style lang="scss">
.login-card {
  width: 20rem;
  padding: 0 2rem;
  background-color: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 5px;

  .header {
    padding: 1rem 0;

    h1 {
      font-size: 1.4rem;
      color: #333;
      text-align: center;
      letter-spacing: 1px;
    }
  }

  .footer {
    display: flex;
    justify-content: space-between;
    padding-bottom: 1.4rem;
  }
}
</style>
