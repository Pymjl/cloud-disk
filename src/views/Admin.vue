<script lang="ts">
import { defineComponent } from 'vue'
import TopNav from '@/components/public/TopNav.vue'

export default defineComponent({
  name: 'AdminPage',
  components: { TopNav },
  setup() {
    // Code here
  }
})
</script>

<template>
  <TopNav />
  <p>AdminPage</p>
</template>
