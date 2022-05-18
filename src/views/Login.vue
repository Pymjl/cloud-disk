<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useMessage } from 'naive-ui'
import BgWrapper from '@/components/login/BgWrapper.vue'
import ImageVerify from '@/components/login/ImageVerify.vue'
import EmailVerify from '@/components/login/EmailVerify.vue'

export default defineComponent({
  name: 'LoginPage',
  components: { BgWrapper, ImageVerify, EmailVerify },
  setup() {
    // 表单验证
    const message = useMessage()
    const formRef = ref(null)
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
    return {
      message,
      formRef,
      formValue,
      rules
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
          <NButton secondary type="info">新建账号</NButton>
          <NButton type="primary">登录</NButton>
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
