<template>
  <main class="page">
    <header class="page-header">
      <el-button text :icon="ArrowLeft" @click="$router.push('/')">首页</el-button>
      <div>
        <p class="eyebrow">Games</p>
        <h1>{{ title }}</h1>
      </div>
    </header>

    <el-skeleton :loading="loading" animated :rows="6">
      <template #default>
        <el-empty v-if="!games.length" description="暂无游戏" />
        <section v-else class="game-grid">
          <GameCard
            v-for="game in games"
            :key="game.id"
            :game="game"
            @click="goGallery(game)"
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
  </main>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { pageGames } from '../api/gameApi'
import GameCard from '../components/GameCard.vue'

const route = useRoute()
const router = useRouter()

const games = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = 12
const total = ref(0)

const type = computed(() => route.query.type || 'character')
const title = computed(() => {
  if (type.value === 'character') {
    return '立绘游戏列表'
  }
  if (type.value === 'screenshot') {
    return '截图游戏列表'
  }
  return '照片/截图游戏列表'
})

async function fetchGames() {
  loading.value = true
  try {
    const result = await pageGames({
      page: page.value,
      size: pageSize,
      type: type.value
    })
    games.value = result?.records || []
    total.value = result?.total || 0
  } catch (error) {
    ElMessage.error(error.message)
  } finally {
    loading.value = false
  }
}

function handlePageChange(nextPage) {
  page.value = nextPage
  fetchGames()
}

function goGallery(game) {
  router.push({
    path: `/gallery/${game.id}`,
    query: { type: type.value }
  })
}

watch(
  () => route.query.type,
  () => {
    page.value = 1
    fetchGames()
  }
)

onMounted(fetchGames)
</script>
