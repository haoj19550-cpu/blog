<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getArticleListApi } from '../api/article'
import { getUserInfo } from '../utils/auth'
import type { ArticleListItem, PageResponse } from '../types/api'

const router = useRouter()
const user = ref(getUserInfo())
const loading = ref(false)
const total = ref(0)
const list = ref<ArticleListItem[]>([])

const query = reactive({
  pageNum: 1,
  pageSize: 6,
  keyword: '',
})

const loadArticles = async () => {
  loading.value = true
  try {
    const data: PageResponse<ArticleListItem> = await getArticleListApi({
      pageNum: query.pageNum,
      pageSize: query.pageSize,
      keyword: query.keyword || undefined,
    })
    list.value = data.list || []
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

const goRead = (id: string) => {
  router.push(`/articles/${id}`)
}

const goEdit = (id: string) => {
  router.push(`/editor?id=${id}`)
}

const handleSearch = async () => {
  query.pageNum = 1
  await loadArticles()
}

const handlePageChange = async (pageNum: number) => {
  query.pageNum = pageNum
  await loadArticles()
}

onMounted(loadArticles)
</script>

<template>
  <div class="page-container">
    <div class="page-head">
      <h1>个人中心</h1>
      <el-button type="primary" @click="router.push('/editor')">写新文章</el-button>
    </div>

    <el-row :gutter="16" class="profile-top-grid">
      <el-col :xs="24" :md="10">
        <el-card shadow="never" class="profile-card">
          <div class="profile-user">
            <div class="profile-avatar">{{ user?.nickname?.[0] || user?.username?.[0] || 'U' }}</div>
            <div>
              <h3>{{ user?.nickname || '未设置昵称' }}</h3>
              <p>@{{ user?.username || 'unknown' }}</p>
              <p class="profile-id">用户 ID：{{ user?.id || '-' }}</p>
            </div>
          </div>
          <div class="profile-actions">
            <el-button plain @click="router.push('/')">去首页</el-button>
            <el-button type="primary" plain @click="router.push('/editor')">发布文章</el-button>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :md="14">
        <el-card shadow="never" class="profile-card">
          <el-form inline @submit.prevent>
            <el-form-item label="关键词">
              <el-input v-model="query.keyword" clearable placeholder="搜索文章标题/摘要" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="loading" @click="handleSearch">查询文章</el-button>
            </el-form-item>
          </el-form>

          <el-empty v-if="!loading && list.length === 0" description="暂无可展示文章" />
          <div v-else class="article-list profile-article-list">
            <el-card v-for="item in list" :key="item.id" class="article-card" shadow="hover">
              <div class="article-card-head">
                <h3 class="article-title" @click="goRead(item.id)">{{ item.title }}</h3>
                <span class="article-meta">阅读 {{ item.viewCount || 0 }}</span>
              </div>
              <p class="article-summary">{{ item.summary || '暂无摘要。' }}</p>
              <div class="article-meta-row">
                <span>{{ item.updatedAt || item.createdAt || '-' }}</span>
                <div class="profile-row-actions">
                  <el-button link type="primary" @click="goRead(item.id)">查看</el-button>
                  <el-button link @click="goEdit(item.id)">编辑</el-button>
                </div>
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
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
