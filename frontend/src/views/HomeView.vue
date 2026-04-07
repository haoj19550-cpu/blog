<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getArticleListApi } from '../api/article'
import type { ArticleListItem, PageResponse } from '../types/api'

const router = useRouter()
const loading = ref(false)
const list = ref<ArticleListItem[]>([])
const total = ref(0)

const query = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  categoryId: '',
})

const loadArticles = async () => {
  loading.value = true
  try {
    const data: PageResponse<ArticleListItem> = await getArticleListApi({
      pageNum: query.pageNum,
      pageSize: query.pageSize,
      keyword: query.keyword || undefined,
      categoryId: query.categoryId || undefined,
    })
    list.value = data.list || []
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

const handleSearch = async () => {
  query.pageNum = 1
  await loadArticles()
}

const handlePageChange = async (pageNum: number) => {
  query.pageNum = pageNum
  await loadArticles()
}

const goDetail = (id: string) => {
  router.push(`/articles/${id}`)
}

onMounted(loadArticles)
</script>

<template>
  <div class="page-container">
    <div class="page-head">
      <h1>文章广场</h1>
      <RouterLink class="solid-btn" to="/editor">写文章</RouterLink>
    </div>

    <el-card shadow="never" class="filter-card">
      <el-form inline @submit.prevent>
        <el-form-item label="关键词">
          <el-input v-model="query.keyword" clearable placeholder="搜索标题/摘要" />
        </el-form-item>
        <el-form-item label="分类ID">
          <el-input v-model="query.categoryId" clearable placeholder="可选" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleSearch">搜索</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-empty v-if="!loading && list.length === 0" description="暂无文章" />
    <div v-else class="article-list">
      <el-card v-for="item in list" :key="item.id" class="article-card" shadow="hover">
        <div class="article-card-head">
          <h3 class="article-title" @click="goDetail(item.id)">{{ item.title }}</h3>
          <span class="article-meta">阅读 {{ item.viewCount || 0 }}</span>
        </div>
        <p class="article-summary">{{ item.summary || '作者还没有填写摘要。' }}</p>
        <div class="article-meta-row">
          <span>{{ item.createdAt || '-' }}</span>
          <el-button link type="primary" @click="goDetail(item.id)">阅读详情</el-button>
        </div>
      </el-card>
    </div>

    <div class="pagination-wrap">
      <el-pagination
        background
        layout="prev, pager, next, total"
        :current-page="query.pageNum"
        :page-size="query.pageSize"
        :total="total"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>
