<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { registerApi } from '../api/auth'
import { setToken, setUserInfo } from '../utils/auth'
import type { RegisterRequest } from '../types/api'

interface RegisterForm extends RegisterRequest {
  confirmPassword: string
}

const router = useRouter()
const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive<RegisterForm>({
  username: '',
  password: '',
  nickname: '',
  confirmPassword: '',
})

const rules: FormRules<RegisterForm> = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, message: '用户名至少 3 位', trigger: 'blur' },
  ],
  nickname: [{ min: 2, message: '昵称至少 2 位', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少 6 位', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (value !== form.password) {
          callback(new Error('两次输入密码不一致'))
          return
        }
        callback()
      },
      trigger: 'blur',
    },
  ],
}

const submit = async () => {
  if (!formRef.value) return
  await formRef.value.validate()
  loading.value = true
  try {
    const payload: RegisterRequest = {
      username: form.username,
      password: form.password,
      nickname: form.nickname || undefined,
    }
    const data = await registerApi(payload)
    setToken(data.token)
    setUserInfo(data.userInfo)
    ElMessage.success('注册成功，已自动登录')
    await router.replace('/')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="page-container auth-page">
    <el-card shadow="never" class="auth-card">
      <template #header>
        <h2 class="auth-title">创建账号</h2>
      </template>
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top" @submit.prevent>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="昵称（可选）" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称" clearable />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="form.confirmPassword"
            type="password"
            show-password
            placeholder="请再次输入密码"
          />
        </el-form-item>
        <el-button class="auth-submit-btn" type="primary" :loading="loading" @click="submit">
          注册并登录
        </el-button>
      </el-form>
      <div class="auth-footer">
        已有账号？
        <RouterLink to="/login">去登录</RouterLink>
      </div>
    </el-card>
  </div>
</template>
