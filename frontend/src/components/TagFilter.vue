<template>
  <section class="tag-filter">
    <span class="filter-label">标签筛选</span>
    <el-check-tag
      v-for="tag in tags"
      :key="tag.id"
      :checked="modelValue.includes(tag.id)"
      @change="toggleTag(tag.id)"
    >
      {{ tag.name }}
    </el-check-tag>
    <el-button v-if="modelValue.length" size="small" text @click="clearTags">清空</el-button>
  </section>
</template>

<script setup>
const props = defineProps({
  modelValue: {
    type: Array,
    default: () => []
  },
  tags: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['update:modelValue', 'change'])

function toggleTag(tagId) {
  const next = props.modelValue.includes(tagId)
    ? props.modelValue.filter((id) => id !== tagId)
    : [...props.modelValue, tagId]
  emit('update:modelValue', next)
  emit('change', next)
}

function clearTags() {
  emit('update:modelValue', [])
  emit('change', [])
}
</script>
