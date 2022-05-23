<script lang="ts">
import { Component, defineComponent, h, ref, watch } from 'vue'
import { DropdownOption, NIcon } from 'naive-ui'
import { FolderAdd, DocumentAdd } from '@vicons/carbon'

export default defineComponent({
  name: 'CustomDropdown',
  props: {
    flag: {
      type: Boolean,
      required: true
    },
    type: {
      type: String,
      required: true
    },
    x: {
      type: Number,
      required: true
    },
    y: {
      type: Number,
      required: true
    }
  },
  setup(props) {
    const showDropdown = ref(false)
    const options = [] as DropdownOption[]

    // 图标渲染工具函数
    function renderIcon(icon: Component) {
      return () => h(NIcon, null, { default: () => h(icon) })
    }

    watch(
      () => props.flag,
      () => {
        showDropdown.value = true
        options.splice(0)
        switch (props.type) {
          case 'new':
            options.push({
              label: '新建文件夹',
              icon: renderIcon(FolderAdd),
              key: 'new-folder'
            })
            options.push({
              label: '新建文件',
              icon: renderIcon(DocumentAdd),
              key: 'new-file'
            })
            break
        }
      }
    )

    const handleSelect = () => {
      showDropdown.value = false
    }
    const onClickOutside = () => {
      showDropdown.value = false
    }

    return { showDropdown, options, handleSelect, onClickOutside }
  }
})
</script>

<template>
  <NDropdown
    placement="bottom-start"
    trigger="manual"
    :x="x"
    :y="y"
    :options="options"
    :show="showDropdown"
    :on-clickoutside="onClickOutside"
    @select="handleSelect"
  />
</template>
