<template>
  <main class="page home-page">
    <section class="home-shell">
      <p class="eyebrow">GalGallery</p>
      <h1>图片收藏管理</h1>
      <div class="entry-grid">
        <router-link class="entry-card" :to="{ path: '/games', query: { type: 'character' } }">
          <span>查看立绘</span>
        </router-link>
        <router-link class="entry-card" :to="{ path: '/games', query: { type: 'photo' } }">
          <span>查看照片/截图</span>
        </router-link>
      </div>
      <el-button class="admin-link" type="primary" plain @click="$router.push('/admin')">
        后台管理
      </el-button>
    </section>
  </main>
</template>

