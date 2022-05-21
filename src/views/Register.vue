<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useRouter } from 'vue-router'
import { FormInst, FormItemRule, useLoadingBar, useMessage } from 'naive-ui'
import BgWrapper from '@/components/login/BgWrapper.vue'
import ImageVerify from '@/components/login/ImageVerify.vue'
import EmailVerify from '@/components/login/EmailVerify.vue'
import { register } from '@/api/login'

export default defineComponent({
  name: 'RegisterPage',
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
      nickname: '',
      password: '',
      confirm: '',
      imageVerify: '',
      emailVerify: ''
    })
    const rules = {
      username: {
        required: true,
        message: '请输入邮箱地址',
        trigger: 'input'
      },
      nickname: {
        required: true,
        message: '请输入昵称',
        trigger: 'input'
      },
      password: {
        required: true,
        message: '请输入密码',
        trigger: 'input'
      },
      confirm: {
        required: true,
        validator(rule: FormItemRule, value: string) {
          if (!value) {
            return new Error('请确认密码')
          }
          if (formValue.value.password !== formValue.value.confirm) {
            return new Error('两次输入的密码不一致')
          }
          return true
        },
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
          // 表单验证通过即可注册
          register(formValue.value.username, formValue.value.nickname, formValue.value.password, formValue.value.emailVerify).then(
            ({ succeed, res }) => {
              if (succeed) {
                message.success('注册成功')
                goTo('/login')
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
                message.error('注册请求发送失败，请检查您的网络')
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
    <div class="register-card">
      <div class="header">
        <h1>企业云盘 - 注册</h1>
      </div>
      <div class="main">
        <NForm ref="formRef" :show-label="false" :model="formValue" :rules="rules">
          <NFormItem path="username">
            <NInput v-model:value="formValue.username" placeholder="邮箱地址" autofocus />
          </NFormItem>
          <NFormItem path="nickname">
            <NInput v-model:value="formValue.nickname" placeholder="昵称" />
          </NFormItem>
          <NFormItem path="password">
            <NInput v-model:value="formValue.password" type="password" show-password-on="mousedown" placeholder="密码" />
          </NFormItem>
          <NFormItem path="confirm">
            <NInput v-model:value="formValue.confirm" type="password" show-password-on="mousedown" placeholder="确认密码" />
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
          <NButton secondary type="info" @click="goTo('/')">已有账号</NButton>
          <NButton type="primary" @click="handleValidateClick">注册</NButton>
        </div>
      </div>
    </div>
  </BgWrapper>
</template>

<style lang="scss">
.register-card {
  width: 20rem;
  padding: 0 2rem;
  background-color: #fff;
  border: 1px solid #eee;
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
