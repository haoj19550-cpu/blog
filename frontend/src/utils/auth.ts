import type { UserInfo } from '../types/api'

const TOKEN_KEY = 'blog_token'
const USER_KEY = 'blog_user'
const AUTH_CHANGED_EVENT = 'blog-auth-changed'

const notifyAuthChanged = (): void => {
  window.dispatchEvent(new Event(AUTH_CHANGED_EVENT))
}

export const getToken = (): string => localStorage.getItem(TOKEN_KEY) ?? ''

export const setToken = (token: string): void => {
  localStorage.setItem(TOKEN_KEY, token)
  notifyAuthChanged()
}

export const clearToken = (): void => {
  localStorage.removeItem(TOKEN_KEY)
  notifyAuthChanged()
}

export const setUserInfo = (user: UserInfo): void => {
  localStorage.setItem(USER_KEY, JSON.stringify(user))
  notifyAuthChanged()
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
  notifyAuthChanged()
}

export const isLoggedIn = (): boolean => Boolean(getToken())

export const logout = (): void => {
  clearToken()
  clearUserInfo()
}

export const onAuthChanged = (listener: () => void): (() => void) => {
  window.addEventListener(AUTH_CHANGED_EVENT, listener)
  return () => window.removeEventListener(AUTH_CHANGED_EVENT, listener)
}
