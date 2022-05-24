<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'FolderSelect',
  emits: ['selected'],
  setup(props, { emit }) {
    const handleChange = (event: Event) => {
      emit('selected', (event.target as HTMLSelectElement).value)
    }

    return {
      handleChange
    }
  }
})
</script>

<template>
  <div>FolderSelect</div>
</template>

<style lang="scss">
// Code here
</style>
