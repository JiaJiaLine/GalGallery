<template>
  <main class="page">
    <header class="page-header">
      <el-button text @click="$router.push('/')">返回首页</el-button>
      <h1>后台管理</h1>
    </header>
    <el-tabs>
      <el-tab-pane label="游戏管理">
        <el-empty description="游戏管理接口将在下一步接入" />
      </el-tab-pane>
      <el-tab-pane label="图片管理">
        <el-empty description="图片上传接口将在下一步接入" />
      </el-tab-pane>
      <el-tab-pane label="标签管理">
        <el-empty description="标签管理接口将在下一步接入" />
      </el-tab-pane>
    </el-tabs>
  </main>
</template>

