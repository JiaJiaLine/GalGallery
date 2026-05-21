<template>
  <main class="page">
    <header class="page-header">
      <el-button text @click="$router.push('/')">返回首页</el-button>
      <h1>{{ title }}</h1>
    </header>
    <el-empty description="游戏列表接口将在下一步接入" />
  </main>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const title = computed(() => {
  const type = route.query.type
  if (type === 'character') {
    return '立绘游戏列表'
  }
  if (type === 'screenshot') {
    return '截图游戏列表'
  }
  return '照片/截图游戏列表'
})
</script>

