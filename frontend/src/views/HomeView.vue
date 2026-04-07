<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getArticleListApi } from '../api/article'
import { getUserInfo } from '../utils/auth'
import type { ArticleListItem, PageResponse } from '../types/api'

const router = useRouter()
const loading = ref(false)
const list = ref<ArticleListItem[]>([])
const total = ref(0)
const currentUser = ref(getUserInfo())

const query = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  categoryId: '',
})
const activeSort = ref<'new' | 'hot' | 'random'>('new')

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
  } catch {
    list.value = []
    total.value = 0
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

const authorName = computed(() => currentUser.value?.nickname || currentUser.value?.username || 'Duanho')

onMounted(() => {
  void loadArticles()
})
</script>

<template>
  <div class="page-container home-page">
    <section class="home-main">
      <div class="home-breadcrumb">📍 现在位置：主页</div>
      <div class="home-sort">
        <span class="sort-label">排序</span>
        <button
          type="button"
          class="sort-item"
          :class="{ 'sort-item--active': activeSort === 'new' }"
          @click="activeSort = 'new'"
        >
          最新
        </button>
        <button
          type="button"
          class="sort-item"
          :class="{ 'sort-item--active': activeSort === 'hot' }"
          @click="activeSort = 'hot'"
        >
          热门
        </button>
        <button
          type="button"
          class="sort-item"
          :class="{ 'sort-item--active': activeSort === 'random' }"
          @click="activeSort = 'random'"
        >
          随机
        </button>
      </div>

      <div class="home-toolbar">
        <h2>最近文章</h2>
        <div class="home-toolbar-actions">
          <el-input v-model="query.keyword" clearable placeholder="搜索标题/摘要" />
          <el-button type="primary" plain :loading="loading" @click="handleSearch">搜索</el-button>
        </div>
      </div>
      <div class="home-tags">
        <span class="home-tag">日志</span>
        <span class="home-tag">生活</span>
        <span class="home-tag">编程</span>
        <span class="home-tag">随笔</span>
      </div>

      <el-empty v-if="!loading && list.length === 0" description="暂无文章" />
      <div v-else class="article-list">
        <article v-for="item in list" :key="item.id" class="article-entry">
          <h3 class="article-entry-title" @click="goDetail(item.id)">
            {{ item.title }}
          </h3>
          <p class="article-entry-summary">{{ item.summary || '作者还没有填写摘要。' }}</p>
          <div class="article-author-line">
            <span class="article-avatar-mini">{{ authorName[0] }}</span>
            <span>{{ authorName }}</span>
          </div>
          <div class="article-meta-row">
            <span>{{ item.createdAt || '-' }}</span>
            <span>阅读 {{ item.viewCount || 0 }}</span>
            <el-button link type="primary" @click="goDetail(item.id)">阅读全文</el-button>
          </div>
        </article>
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
    </section>

    <aside class="home-aside">
      <section class="aside-card author-card">
        <div class="author-avatar">{{ authorName[0] }}</div>
        <h3>{{ authorName }}</h3>
        <p>前端工程师 / 生活记录者 / 持续更新</p>
        <div class="author-social">
          <span>Github</span>
          <span>Mail</span>
          <span>RSS</span>
        </div>
      </section>
      <section class="aside-card site-stats">
        <h4>站点统计</h4>
        <p>文章数：{{ total }}</p>
        <p>在线访客：0</p>
        <p>最近更新：今天</p>
      </section>
      <section class="aside-card write-entry">
        <h4>创作入口</h4>
        <RouterLink class="solid-btn" to="/editor">写一篇新文章</RouterLink>
      </section>
    </aside>
  </div>
</template>
