<script lang="ts">
import { defineComponent, inject, ref, Ref } from 'vue'
import { FormInst, FormItemRule, useMessage } from 'naive-ui'
import { changePassword } from '@/api/user'

export default defineComponent({
  name: 'PersonalInformation',
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

    // 表单验证
    const formRef = ref<FormInst | null>(null)
    const formValue = ref({
      password: '',
      confirm: ''
    })
    const rules = {
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
      }
    }
    const handleValidateClick = (e: MouseEvent) => {
      e.preventDefault()
      formRef.value?.validate((errors) => {
        if (!errors) {
          // 表单验证通过即可注册
          changePassword(formValue.value.password).then(
            ({ succeed, res }) => {
              if (succeed) {
                message.success('密码修改成功')
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

    return { userInfo, formRef, formValue, rules, handleValidateClick }
  }
})
</script>

<template>
  <NCollapse :default-expanded-names="['avatar', 'nickname', 'password']">
    <NCollapseItem title="头像设置" name="avatar">
      <template #header-extra>您在本系统使用的头像</template>
      <div style="display: flex; align-items: center; justify-content: space-evenly">
        <n-avatar round :size="96" :src="userInfo.avatar" />
        <NButton secondary type="info">修改头像</NButton>
      </div>
    </NCollapseItem>
    <NCollapseItem title="昵称设置" name="nickname">
      <template #header-extra>您在本系统使用的昵称</template>
      <NInputGroup>
        <NInput :default-value="userInfo.nickname" placeholder="请输入昵称" />
        <NButton type="info">修改昵称</NButton>
      </NInputGroup>
    </NCollapseItem>
    <NCollapseItem title="密码设置" name="password">
      <template #header-extra>您的账户安全至关重要</template>
      <NForm ref="formRef" :show-label="false" :model="formValue" :rules="rules">
        <NFormItem path="password">
          <NInput v-model:value="formValue.password" type="password" show-password-on="mousedown" placeholder="密码" />
        </NFormItem>
        <NFormItem path="confirm">
          <NInput v-model:value="formValue.confirm" type="password" show-password-on="mousedown" placeholder="确认密码" />
        </NFormItem>
        <NFormItem style="float: right">
          <NButton type="warning">修改密码</NButton>
        </NFormItem>
      </NForm>
    </NCollapseItem>
  </NCollapse>
</template>
