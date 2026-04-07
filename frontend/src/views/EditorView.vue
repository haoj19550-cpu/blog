<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { createArticleApi, getArticleDetailApi, updateArticleApi } from '../api/article'
import type { ArticleCreateRequest } from '../types/api'

interface EditorForm {
  title: string
  summary: string
  content: string
  categoryId: number | undefined
  coverImage: string
  status: number
  isTop: number
  tagIdsRaw: string
}

const route = useRoute()
const router = useRouter()
const formRef = ref<FormInstance>()
const loading = ref(false)
const submitting = ref(false)

const articleId = computed(() => {
  const id = route.query.id
  return typeof id === 'string' && id ? id : ''
})
const isEditMode = computed(() => Boolean(articleId.value))

const form = reactive<EditorForm>({
  title: '',
  summary: '',
  content: '',
  categoryId: undefined,
  coverImage: '',
  status: 1,
  isTop: 0,
  tagIdsRaw: '',
})

const rules: FormRules<EditorForm> = {
  title: [
    { required: true, message: '请输入标题', trigger: 'blur' },
    { min: 2, message: '标题至少 2 个字符', trigger: 'blur' },
  ],
  content: [{ required: true, message: '请输入正文', trigger: 'blur' }],
}

const fillByDetail = async () => {
  if (!articleId.value) return
  loading.value = true
  try {
    const detail = await getArticleDetailApi(articleId.value)
    form.title = detail.title
    form.summary = detail.summary || ''
    form.content = detail.content || ''
    form.coverImage = detail.coverImage || ''
    form.categoryId = detail.categoryId ? Number(detail.categoryId) : undefined
    form.tagIdsRaw = (detail.tagIds || []).join(',')
  } finally {
    loading.value = false
  }
}

const buildPayload = (): ArticleCreateRequest => {
  const tagIds = form.tagIdsRaw
    .split(',')
    .map((item) => Number(item.trim()))
    .filter((item) => Number.isFinite(item))

  return {
    title: form.title,
    summary: form.summary || undefined,
    content: form.content,
    categoryId: form.categoryId,
    coverImage: form.coverImage || undefined,
    status: form.status,
    isTop: form.isTop,
    tagIds: tagIds.length ? tagIds : undefined,
  }
}

const submit = async () => {
  if (!formRef.value) return
  await formRef.value.validate()
  submitting.value = true
  try {
    const payload = buildPayload()
    if (isEditMode.value) {
      await updateArticleApi(articleId.value, payload)
      ElMessage.success('文章更新成功')
      await router.push(`/articles/${articleId.value}`)
      return
    }
    const id = await createArticleApi(payload)
    ElMessage.success('文章发布成功')
    await router.push(`/articles/${id}`)
  } finally {
    submitting.value = false
  }
}

onMounted(fillByDetail)
</script>

<template>
  <div class="page-container">
    <div class="page-head">
      <h1>{{ isEditMode ? '编辑文章' : '发布文章' }}</h1>
    </div>
    <el-card shadow="never" v-loading="loading">
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top" @submit.prevent>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" maxlength="120" show-word-limit placeholder="请输入文章标题" />
        </el-form-item>
        <el-form-item label="摘要">
          <el-input
            v-model="form.summary"
            type="textarea"
            :rows="3"
            maxlength="300"
            show-word-limit
            placeholder="请输入文章摘要（可选）"
          />
        </el-form-item>
        <el-form-item label="正文" prop="content">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="14"
            placeholder="请输入正文内容（支持纯文本/Markdown）"
          />
        </el-form-item>
        <div class="editor-grid">
          <el-form-item label="分类ID">
            <el-input-number v-model="form.categoryId" :min="1" :step="1" controls-position="right" />
          </el-form-item>
          <el-form-item label="标签ID（英文逗号分隔）">
            <el-input v-model="form.tagIdsRaw" placeholder="例如 1,2,3" />
          </el-form-item>
          <el-form-item label="封面图 URL">
            <el-input v-model="form.coverImage" placeholder="可选" />
          </el-form-item>
          <el-form-item label="发布状态">
            <el-select v-model="form.status">
              <el-option :value="0" label="草稿" />
              <el-option :value="1" label="已发布" />
            </el-select>
          </el-form-item>
          <el-form-item label="置顶">
            <el-switch v-model="form.isTop" :active-value="1" :inactive-value="0" />
          </el-form-item>
        </div>
        <el-button type="primary" :loading="submitting" @click="submit">
          {{ isEditMode ? '保存更新' : '发布文章' }}
        </el-button>
      </el-form>
    </el-card>
  </div>
</template>
