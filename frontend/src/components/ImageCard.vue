<template>
  <button class="image-card" type="button">
    <div class="image-card-media">
      <img :src="image.thumbnailUrl || image.imageUrl" :alt="image.title || 'gallery image'" />
    </div>
    <div class="image-card-body">
      <h2>{{ image.title || '未命名图片' }}</h2>
      <p>{{ image.description || typeLabel }}</p>
      <div v-if="image.tags?.length" class="tag-row">
        <el-tag v-for="tag in image.tags" :key="tag.id" size="small" effect="plain">
          {{ tag.name }}
        </el-tag>
      </div>
    </div>
  </button>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  image: {
    type: Object,
    required: true
  }
})

const typeLabel = computed(() => {
  if (props.image.type === 'character') {
    return '立绘'
  }
  if (props.image.type === 'screenshot') {
    return '截图'
  }
  return '照片'
})
</script>
