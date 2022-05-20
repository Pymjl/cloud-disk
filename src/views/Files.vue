<script lang="ts">
import { defineComponent, inject, Ref } from 'vue'
import TopNav from '@/components/public/TopNav.vue'

export default defineComponent({
  name: 'FilesPage',
  components: { TopNav },
  setup() {
    // 抽屉控制
    const personalActive = inject('personalActive') as Ref<boolean>
    const adminActive = inject('adminActive') as Ref<boolean>

    return { personalActive, adminActive }
  }
})
</script>

<template>
  <TopNav />
  <p>FilesPage</p>
  <n-drawer v-model:show="adminActive" :width="768">
    <n-drawer-content>
      <template #header>管理面板</template>
      <template #footer>
        <n-button>Footer</n-button>
      </template>
    </n-drawer-content>
  </n-drawer>
  <n-drawer v-model:show="personalActive" :width="512">
    <n-drawer-content>
      <template #header>个人信息</template>
      <template #footer>
        <n-button>Footer</n-button>
      </template>
    </n-drawer-content>
  </n-drawer>
</template>
