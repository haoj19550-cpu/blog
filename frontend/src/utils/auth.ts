import type { UserInfo } from '../types/api'

const TOKEN_KEY = 'blog_token'
const USER_KEY = 'blog_user'

export const getToken = (): string => localStorage.getItem(TOKEN_KEY) ?? ''

export const setToken = (token: string): void => {
  localStorage.setItem(TOKEN_KEY, token)
}

export const clearToken = (): void => {
  localStorage.removeItem(TOKEN_KEY)
}

export const setUserInfo = (user: UserInfo): void => {
  localStorage.setItem(USER_KEY, JSON.stringify(user))
}

export const getUserInfo = (): UserInfo | null => {
  const raw = localStorage.getItem(USER_KEY)
  if (!raw) return null

  try {
    return JSON.parse(raw) as UserInfo
  } catch {
    return null
  }
}

export const clearUserInfo = (): void => {
  localStorage.removeItem(USER_KEY)
}

export const isLoggedIn = (): boolean => Boolean(getToken())

export const logout = (): void => {
  clearToken()
  clearUserInfo()
}
