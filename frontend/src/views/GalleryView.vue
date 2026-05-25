<template>
  <main class="page">
    <header class="page-header">
      <el-button text :icon="ArrowLeft" @click="$router.back()">返回</el-button>
      <div>
        <p class="eyebrow">Gallery</p>
        <h1>{{ title }}</h1>
      </div>
    </header>

    <TagFilter v-model="selectedTagIds" :tags="tags" @change="handleTagChange" />

    <el-skeleton :loading="loading" animated :rows="8">
      <template #default>
        <el-empty v-if="!images.length" description="暂无图片" />
        <section v-else class="image-grid">
          <ImageCard
            v-for="image in images"
            :key="image.id"
            :image="image"
            @click="openPreview(image)"
          />
        </section>

        <div v-if="total > pageSize" class="pagination-row">
          <el-pagination
            background
            layout="prev, pager, next"
            :current-page="page"
            :page-size="pageSize"
            :total="total"
            @current-change="handlePageChange"
          />
        </div>
      </template>
    </el-skeleton>

    <ImagePreview v-model="previewVisible" :image="currentImage" />
  </main>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { pageImages } from '../api/imageApi'
import { listAllTags } from '../api/tagApi'
import ImageCard from '../components/ImageCard.vue'
import ImagePreview from '../components/ImagePreview.vue'
import TagFilter from '../components/TagFilter.vue'

const props = defineProps({
  gameId: {
    type: String,
    required: true
  }
})

const route = useRoute()
const images = ref([])
const tags = ref([])
const selectedTagIds = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = 12
const total = ref(0)
const previewVisible = ref(false)
const currentImage = ref(null)

const type = computed(() => route.query.type || 'character')
const title = computed(() => {
  if (type.value === 'character') {
    return '立绘展示'
  }
  if (type.value === 'screenshot') {
    return '截图展示'
  }
  return '照片/截图展示'
})

async function fetchImages() {
  loading.value = true
  try {
    const result = await pageImages({
      gameId: props.gameId,
      type: type.value,
      tagIds: selectedTagIds.value.join(','),
      page: page.value,
      size: pageSize
    })
    images.value = result?.records || []
    total.value = result?.total || 0
  } catch (error) {
    ElMessage.error(error.message)
  } finally {
    loading.value = false
  }
}

async function fetchTags() {
  try {
    tags.value = await listAllTags()
  } catch (error) {
    ElMessage.error(error.message)
  }
}

function handleTagChange() {
  page.value = 1
  fetchImages()
}

function handlePageChange(nextPage) {
  page.value = nextPage
  fetchImages()
}

function openPreview(image) {
  currentImage.value = image
  previewVisible.value = true
}

watch(
  () => [props.gameId, route.query.type],
  () => {
    page.value = 1
    fetchImages()
  }
)

onMounted(() => {
  fetchTags()
  fetchImages()
})
</script>
