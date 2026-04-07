import http from './http'
import type {
  ArticleCreateRequest,
  ArticleDetailResponse,
  ArticleListItem,
  ArticleListQuery,
  ArticleUpdateRequest,
  PageResponse,
} from '../types/api'

export const getArticleListApi = (params: ArticleListQuery) => {
  return http.get<unknown, PageResponse<ArticleListItem>>('/api/articles', { params })
}

export const getArticleDetailApi = (articleId: string | number) => {
  return http.get<unknown, ArticleDetailResponse>(`/api/articles/${articleId}`)
}

export const createArticleApi = (payload: ArticleCreateRequest) => {
  return http.post<ArticleCreateRequest, number>('/api/articles', payload)
}

export const updateArticleApi = (
  articleId: string | number,
  payload: ArticleUpdateRequest,
) => {
  return http.put<ArticleUpdateRequest, void>(`/api/articles/${articleId}`, payload)
}

export const deleteArticleApi = (articleId: string | number) => {
  return http.delete<unknown, void>(`/api/articles/${articleId}`)
}
