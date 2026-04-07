export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

export interface PageResponse<T> {
  list: T[]
  total: number
  pageNum: number
  pageSize: number
}

export interface UserInfo {
  id: string
  username: string
  nickname?: string
  avatar?: string
}

export interface AuthResponse {
  token: string
  userInfo: UserInfo
}

export interface LoginRequest {
  username: string
  password: string
}

export interface RegisterRequest {
  username: string
  password: string
  nickname?: string
}

export interface ArticleListQuery {
  pageNum?: number
  pageSize?: number
  categoryId?: string | number
  keyword?: string
}

export interface ArticleListItem {
  id: string
  title: string
  content?: string
  summary?: string
  coverImage?: string
  viewCount?: number
  createdAt?: string
  updatedAt?: string
}

export interface ArticleDetailResponse extends ArticleListItem {
  content: string
  categoryId?: string
  tagIds?: string[]
}

export interface ArticleCreateRequest {
  title: string
  content: string
  summary?: string
  categoryId?: number
  coverImage?: string
  isTop?: number
  status?: number
  tagIds?: number[]
}

export interface ArticleUpdateRequest extends ArticleCreateRequest {}
