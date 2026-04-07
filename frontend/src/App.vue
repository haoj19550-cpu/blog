<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getUserInfo, isLoggedIn, logout, onAuthChanged } from './utils/auth'

const route = useRoute()
const router = useRouter()

const authed = ref(false)
const currentUser = ref(getUserInfo())
const redirectAfterLogin = computed(() => encodeURIComponent(route.fullPath))

const syncAuthState = () => {
  authed.value = isLoggedIn()
  currentUser.value = getUserInfo()
}

const handleLogout = () => {
  logout()
  router.push('/login')
}

let removeAuthListener: (() => void) | null = null

onMounted(() => {
  syncAuthState()
  removeAuthListener = onAuthChanged(syncAuthState)
})

onBeforeUnmount(() => {
  removeAuthListener?.()
})
</script>

<template>
  <div class="app-bg-glow" />
  <div class="app-bg-glow app-bg-glow--secondary" />

  <div class="app-layout">
    <header class="app-nav-wrap">
      <div class="app-nav">
        <RouterLink to="/" class="brand">Neo Gradient Blog</RouterLink>
        <nav class="nav-links">
          <RouterLink to="/" class="nav-link">首页</RouterLink>
          <RouterLink v-if="authed" to="/editor" class="nav-link">写文章</RouterLink>
          <RouterLink v-if="authed" to="/profile" class="nav-link">个人中心</RouterLink>
        </nav>

        <div class="auth-actions">
          <template v-if="authed">
            <span class="user-chip">{{ currentUser?.nickname || currentUser?.username || '已登录用户' }}</span>
            <button class="ghost-btn" type="button" @click="handleLogout">退出登录</button>
          </template>
          <template v-else>
            <RouterLink class="ghost-btn" :to="`/login?redirect=${redirectAfterLogin}`">登录</RouterLink>
            <RouterLink class="solid-btn" to="/register">注册</RouterLink>
          </template>
        </div>
      </div>
    </header>

    <main class="app-main">
      <div class="app-content-card">
        <router-view />
      </div>
    </main>
  </div>
</template>
