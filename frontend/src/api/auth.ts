import http from './http'
import type { AuthResponse, LoginRequest, RegisterRequest } from '../types/api'

export const loginApi = (payload: LoginRequest) => {
  return http.post<LoginRequest, AuthResponse>('/api/auth/login', payload)
}

export const registerApi = (payload: RegisterRequest) => {
  return http.post<RegisterRequest, AuthResponse>('/api/auth/register', payload)
}
