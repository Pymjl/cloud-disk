<script lang="ts">
import { defineComponent, ref, onMounted, inject, Ref } from 'vue'
import { Reset } from '@vicons/carbon'
import { NIcon, NButton, useMessage } from 'naive-ui'
import { API_URL } from '@/config'
import { uuid } from '@/utils/helper'

export default defineComponent({
  name: 'ImageVerify',
  components: {
    NIcon,
    NButton,
    Reset
  },
  setup() {
    const message = useMessage()
    const isLoading = ref(false)
    const verifyCode = ref('')

    // randomId 是从 App.vue 中注入的
    const randomId = inject('randomId') as Ref<string>

    const getCode = () => {
      isLoading.value = true
      // 获取验证码
      fetch(`${API_URL}/codes/image/${randomId.value}`)
        .then((res) => {
          if (res.ok) {
            return res.blob()
          } else {
            message.error('获取图片验证码失败，请检查您的网络连接')
          }
        })
        .then((blob) => {
          if (blob) {
            // revoke 旧验证码 URL
            if (verifyCode.value) URL.revokeObjectURL(verifyCode.value)
            // 设置新验证码 URL
            const url = URL.createObjectURL(blob)
            verifyCode.value = url
          }
        })
        .finally(() => {
          isLoading.value = false
        })
    }

    onMounted(() => {
      randomId.value = uuid()
      getCode()
    })

    return {
      isLoading,
      verifyCode,
      getCode
    }
  }
})
</script>

<template>
  <div class="image-verify">
    <NButton
      strong
      secondary
      :type="isLoading || verifyCode ? 'success' : 'error'"
      :style="verifyCode ? { backgroundImage: `url(${verifyCode})` } : null"
      :loading="isLoading"
      @click="getCode"
    >
      <div v-show="!isLoading && !verifyCode" class="codeMask">
        <NIcon :style="{ marginRight: '3px' }" size="16"><Reset /></NIcon>重新加载
      </div>
    </NButton>
  </div>
</template>

<style lang="scss">
.image-verify {
  margin-left: 8px;

  .codeMask {
    display: flex;
    align-items: center;
  }

  .n-button {
    width: 6rem;
    font-size: 13px;
    background-repeat: no-repeat;
    background-position: center;
    background-size: cover;
  }
}
</style>
