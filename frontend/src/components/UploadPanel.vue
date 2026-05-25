<template>
  <section class="upload-panel">
    <el-form :model="form" label-width="92px" class="admin-form">
      <el-form-item label="游戏">
        <el-select v-model="form.gameId" filterable placeholder="选择游戏">
          <el-option v-for="game in games" :key="game.id" :label="game.name" :value="game.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="分类">
        <el-segmented v-model="form.type" :options="typeOptions" />
      </el-form-item>
      <el-form-item label="标题">
        <el-input v-model="form.title" placeholder="Test Image" />
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="form.description" type="textarea" :rows="3" placeholder="Uploaded from Bruno" />
      </el-form-item>
      <el-form-item label="排序">
        <el-input-number v-model="form.sortOrder" :min="0" />
      </el-form-item>
      <el-form-item label="标签">
        <el-select v-model="form.tagIds" multiple filterable placeholder="选择标签">
          <el-option v-for="tag in tags" :key="tag.id" :label="tag.name" :value="tag.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="图片">
        <el-upload
          ref="uploadRef"
          class="upload-box"
          drag
          action="#"
          accept=".jpg,.jpeg,.png,.webp"
          :auto-upload="false"
          :limit="1"
          :on-change="handleFileChange"
          :on-remove="handleFileRemove"
        >
          <el-icon class="upload-icon"><UploadFilled /></el-icon>
          <div class="el-upload__text">拖入图片或点击选择</div>
        </el-upload>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Upload" :loading="uploading" @click="submitUpload">
          上传图片
        </el-button>
      </el-form-item>
    </el-form>
  </section>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Upload, UploadFilled } from '@element-plus/icons-vue'
import { uploadImage } from '../api/imageApi'

defineProps({
  games: {
    type: Array,
    default: () => []
  },
  tags: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['uploaded'])

const typeOptions = [
  { label: '立绘', value: 'character' },
  { label: '照片', value: 'photo' },
  { label: '截图', value: 'screenshot' }
]

const form = reactive({
  gameId: null,
  type: 'character',
  title: '',
  description: '',
  sortOrder: 0,
  tagIds: []
})

const selectedFile = ref(null)
const uploading = ref(false)
const uploadRef = ref(null)

function handleFileChange(uploadFile) {
  selectedFile.value = uploadFile.raw
}

function handleFileRemove() {
  selectedFile.value = null
}

async function submitUpload() {
  if (!form.gameId) {
    ElMessage.warning('请选择游戏')
    return
  }
  if (!selectedFile.value) {
    ElMessage.warning('请选择图片文件')
    return
  }

  const formData = new FormData()
  formData.append('file', selectedFile.value)
  formData.append('gameId', form.gameId)
  formData.append('type', form.type)
  formData.append('title', form.title)
  formData.append('description', form.description)
  formData.append('sortOrder', form.sortOrder)
  formData.append('tagIds', form.tagIds.join(','))

  uploading.value = true
  try {
    await uploadImage(formData)
    ElMessage.success('图片已上传')
    selectedFile.value = null
    uploadRef.value?.clearFiles()
    Object.assign(form, {
      gameId: form.gameId,
      type: 'character',
      title: '',
      description: '',
      sortOrder: 0,
      tagIds: []
    })
    emit('uploaded')
  } catch (error) {
    ElMessage.error(error.message)
  } finally {
    uploading.value = false
  }
}
</script>
