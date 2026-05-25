<template>
  <el-dialog
    :model-value="modelValue"
    :title="image?.title || '图片预览'"
    width="min(960px, 92vw)"
    append-to-body
    @update:model-value="$emit('update:modelValue', $event)"
  >
    <div class="preview-shell">
      <img v-if="image" :src="image.imageUrl" :alt="image.title || 'preview image'" />
      <p v-if="image?.description">{{ image.description }}</p>
    </div>
  </el-dialog>
</template>

<script setup>
defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  image: {
    type: Object,
    default: null
  }
})

defineEmits(['update:modelValue'])
</script>
