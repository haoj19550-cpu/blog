<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getArticleDetailApi } from '../api/article'
import type { ArticleDetailResponse } from '../types/api'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const article = ref<ArticleDetailResponse | null>(null)

const loadDetail = async () => {
  const id = route.params.id as string
  if (!id) return
  loading.value = true
  try {
    article.value = await getArticleDetailApi(id)
  } finally {
    loading.value = false
  }
}

onMounted(loadDetail)
</script>

<template>
  <div class="page-container">
    <el-skeleton :loading="loading" animated :rows="8">
      <template #default>
        <div v-if="article" class="detail-wrap">
          <div class="detail-head">
            <h1>{{ article.title }}</h1>
            <div class="detail-actions">
              <el-button type="primary" plain @click="router.push(`/editor?id=${article.id}`)">
                编辑
              </el-button>
            </div>
          </div>
          <div class="detail-meta">
            <span>阅读 {{ article.viewCount || 0 }}</span>
            <span>创建时间 {{ article.createdAt || '-' }}</span>
            <span>更新时间 {{ article.updatedAt || '-' }}</span>
          </div>
          <p v-if="article.summary" class="detail-summary">{{ article.summary }}</p>
          <article class="detail-content">
            {{ article.content }}
          </article>
        </div>
        <el-empty v-else description="文章不存在或已删除" />
      </template>
    </el-skeleton>
  </div>
</template>
