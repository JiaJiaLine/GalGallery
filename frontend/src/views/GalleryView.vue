<template>
  <main class="page">
    <header class="page-header">
      <el-button text @click="$router.back()">返回</el-button>
      <h1>图片展示</h1>
    </header>
    <el-empty :description="`游戏 ID：${gameId}，图片接口将在下一步接入`" />
  </main>
</template>

<script setup>
defineProps({
  gameId: {
    type: String,
    required: true
  }
})
</script>

