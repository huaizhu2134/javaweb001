<template>
  <el-tag :type="tagType" :size="size">
    <slot>{{ status }}</slot>
  </el-tag>
</template>

<script setup>
import { computed } from 'vue'
import { getStatusType } from '@/composables/useTable'

const props = defineProps({
  status: {
    type: String,
    required: true
  },
  size: {
    type: String,
    default: 'small'
  }
})

const tagType = computed(() => getStatusType(props.status))
</script>
