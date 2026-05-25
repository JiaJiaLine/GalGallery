<template>
  <button class="game-card" type="button">
    <div class="game-cover-wrap">
      <img v-if="game.coverUrl" class="game-cover" :src="game.coverUrl" :alt="game.name" />
      <div v-else class="cover-placeholder">No Cover</div>
    </div>
    <div class="game-card-body">
      <div class="game-card-title-row">
        <h2>{{ game.name }}</h2>
        <el-tag size="small" effect="plain">{{ game.imageCount || 0 }} 张</el-tag>
      </div>
      <p>{{ game.description || '暂无简介' }}</p>
    </div>
  </button>
</template>

<script setup>
defineProps({
  game: {
    type: Object,
    required: true
  }
})
</script>
