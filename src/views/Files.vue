<script lang="ts">
import { defineComponent, inject, Ref } from 'vue'
import TopNav from '@/components/public/TopNav.vue'
import AdminPanel from '@/components/admin/Admin.vue'
import PersonalInformation from '@/components/user/Personal.vue'

export default defineComponent({
  name: 'FilesPage',
  components: { TopNav, AdminPanel, PersonalInformation },
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
  <div class="files-container">
    <p>FilesPage</p>
  </div>
  <NDrawer v-model:show="adminActive" :width="768">
    <NDrawerContent title="管理面板" closable>
      <AdminPanel />
    </NDrawerContent>
  </NDrawer>
  <NDrawer v-model:show="personalActive" :width="384">
    <NDrawerContent title="个人信息" closable>
      <PersonalInformation />
    </NDrawerContent>
  </NDrawer>
</template>
