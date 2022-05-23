<script lang="ts">
import { Component, defineComponent, h, ref, watch, Ref } from 'vue'
import { DropdownOption, NIcon } from 'naive-ui'
import { FolderAdd, DocumentAdd, Copy, FolderMoveTo, TrashCan, Edit, ResetAlt } from '@vicons/carbon'

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
  emits: ['selected'],
  setup(props, { emit }) {
    const showDropdown = ref(false)
    const options = ref([]) as Ref<DropdownOption[]>

    // 图标渲染工具函数
    function renderIcon(icon: Component) {
      return () => h(NIcon, null, { default: () => h(icon) })
    }

    watch(
      () => props.flag,
      () => {
        showDropdown.value = true
        options.value.splice(0)
        switch (props.type) {
          case 'new':
            options.value.push({
              label: '新建文件夹',
              icon: renderIcon(FolderAdd),
              key: 'new-folder'
            })
            options.value.push({
              label: '上传文件',
              icon: renderIcon(DocumentAdd),
              key: 'upload-file'
            })
            break
          case 'file':
            options.value.push({
              label: '重命名文件',
              icon: renderIcon(Edit),
              key: 'rename-file'
            })
            options.value.push({
              label: '复制文件',
              icon: renderIcon(Copy),
              key: 'copy-file'
            })
            options.value.push({
              label: '移动文件',
              icon: renderIcon(FolderMoveTo),
              key: 'move-file'
            })
            options.value.push({
              label: '删除文件',
              icon: renderIcon(TrashCan),
              key: 'del-file'
            })
            break
          case 'dir':
            options.value.push({
              label: '重命名文件夹',
              icon: renderIcon(Edit),
              key: 'rename-folder'
            })
            options.value.push({
              label: '复制文件夹',
              icon: renderIcon(Copy),
              key: 'copy-folder'
            })
            options.value.push({
              label: '移动文件夹',
              icon: renderIcon(FolderMoveTo),
              key: 'move-folder'
            })
            options.value.push({
              label: '删除文件夹',
              icon: renderIcon(TrashCan),
              key: 'del-folder'
            })
            break
          case 'trash':
            options.value.push({
              label: '恢复',
              icon: renderIcon(ResetAlt),
              key: 'restore'
            })
            options.value.push({
              label: '永久删除',
              icon: renderIcon(TrashCan),
              key: 'delete'
            })
            break
        }
      }
    )

    const handleSelect = (value: string) => {
      emit('selected', value)
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
