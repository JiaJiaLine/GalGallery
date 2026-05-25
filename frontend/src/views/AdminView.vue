<template>
  <main class="page admin-page">
    <header class="page-header">
      <el-button text :icon="ArrowLeft" @click="$router.push('/')">首页</el-button>
      <div>
        <p class="eyebrow">Admin</p>
        <h1>后台管理</h1>
      </div>
    </header>

    <el-tabs v-model="activeTab" class="admin-tabs">
      <el-tab-pane label="游戏管理" name="games">
        <section class="admin-section">
          <el-form :model="gameForm" label-width="92px" class="admin-form">
            <el-form-item label="游戏名">
              <el-input v-model="gameForm.name" placeholder="CLANNAD" />
            </el-form-item>
            <el-form-item label="原名">
              <el-input v-model="gameForm.originalName" placeholder="CLANNAD" />
            </el-form-item>
            <el-form-item label="封面路径">
              <el-input v-model="gameForm.coverUrl" placeholder="/uploads/covers/clannad.webp" />
            </el-form-item>
            <el-form-item label="开发商">
              <el-input v-model="gameForm.developer" placeholder="Key" />
            </el-form-item>
            <el-form-item label="发售日期">
              <el-date-picker
                v-model="gameForm.releaseDate"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="选择日期"
              />
            </el-form-item>
            <el-form-item label="排序">
              <el-input-number v-model="gameForm.sortOrder" :min="0" />
            </el-form-item>
            <el-form-item label="简介">
              <el-input v-model="gameForm.description" type="textarea" :rows="3" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :icon="Plus" :loading="savingGame" @click="submitGame">
                新增游戏
              </el-button>
              <el-button :icon="Refresh" @click="fetchAdminGames">查询游戏</el-button>
            </el-form-item>
          </el-form>

          <el-table :data="adminGames" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="name" label="游戏名" min-width="160" />
            <el-table-column prop="description" label="简介" min-width="220" show-overflow-tooltip />
            <el-table-column prop="imageCount" label="图片数" width="100" />
          </el-table>
        </section>
      </el-tab-pane>

      <el-tab-pane label="标签管理" name="tags">
        <section class="admin-section">
          <el-form :model="tagForm" inline class="tag-form">
            <el-form-item label="名称">
              <el-input v-model="tagForm.name" placeholder="女主" />
            </el-form-item>
            <el-form-item label="颜色">
              <el-color-picker v-model="tagForm.color" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :icon="Plus" :loading="savingTag" @click="submitTag">
                新增标签
              </el-button>
              <el-button :icon="Refresh" @click="fetchAdminTags">查询标签</el-button>
            </el-form-item>
          </el-form>

          <el-table :data="adminTags" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="name" label="名称" min-width="160" />
            <el-table-column label="颜色" width="140">
              <template #default="{ row }">
                <span class="color-swatch" :style="{ backgroundColor: row.color || '#d1d5db' }" />
                {{ row.color || '-' }}
              </template>
            </el-table-column>
          </el-table>
        </section>
      </el-tab-pane>

      <el-tab-pane label="图片上传" name="upload">
        <UploadPanel :games="adminGames" :tags="adminTags" @uploaded="handleImageUploaded" />
      </el-tab-pane>
    </el-tabs>
  </main>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ArrowLeft, Plus, Refresh } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { createGame, pageGames } from '../api/gameApi'
import { createTag, listAllTags, pageTags } from '../api/tagApi'
import UploadPanel from '../components/UploadPanel.vue'

const activeTab = ref('games')
const adminGames = ref([])
const adminTags = ref([])
const savingGame = ref(false)
const savingTag = ref(false)

const gameForm = reactive({
  name: '',
  originalName: '',
  coverUrl: '',
  description: '',
  developer: '',
  releaseDate: '',
  sortOrder: 0
})

const tagForm = reactive({
  name: '',
  color: '#ff6699'
})

async function fetchAdminGames() {
  try {
    const result = await pageGames({ page: 1, size: 100 })
    adminGames.value = result?.records || []
  } catch (error) {
    ElMessage.error(error.message)
  }
}

async function fetchAdminTags() {
  try {
    const pageResult = await pageTags({ page: 1, size: 100 })
    adminTags.value = pageResult?.records?.length ? pageResult.records : await listAllTags()
  } catch (error) {
    ElMessage.error(error.message)
  }
}

async function submitGame() {
  if (!gameForm.name.trim()) {
    ElMessage.warning('请填写游戏名')
    return
  }
  savingGame.value = true
  try {
    await createGame({ ...gameForm })
    ElMessage.success('游戏已新增')
    Object.assign(gameForm, {
      name: '',
      originalName: '',
      coverUrl: '',
      description: '',
      developer: '',
      releaseDate: '',
      sortOrder: 0
    })
    fetchAdminGames()
  } catch (error) {
    ElMessage.error(error.message)
  } finally {
    savingGame.value = false
  }
}

async function submitTag() {
  if (!tagForm.name.trim()) {
    ElMessage.warning('请填写标签名称')
    return
  }
  savingTag.value = true
  try {
    await createTag({ ...tagForm })
    ElMessage.success('标签已新增')
    Object.assign(tagForm, { name: '', color: '#ff6699' })
    fetchAdminTags()
  } catch (error) {
    ElMessage.error(error.message)
  } finally {
    savingTag.value = false
  }
}

function handleImageUploaded() {
  fetchAdminGames()
}

onMounted(() => {
  fetchAdminGames()
  fetchAdminTags()
})
</script>
