import { api } from '../api/api.ts'
import type { CreateUrlRequest, UpdateUrlRequest, CreateDemoUrlRequest } from '../types.ts'

export const urlService = {
    getUserUrls: async () => {
        const { data } = await api.get('/urls')
        return data

    },

    createUrl: async (payload: CreateUrlRequest) => {
        const { data } = await api.post('/urls', payload)
        return data
    },

    updateUrl: async (payload: UpdateUrlRequest) => {
        const { id, name, originalUrl } = payload
        const { data } = await api.put(`/urls/${id}`, { name, originalUrl })
        return data
    },

    deleteUrl: async (id: number) => {
        const { data } = await api.delete(`/urls/${id}`)
        return data
    },

    createDemoUrl: async (payload: CreateDemoUrlRequest) => {
        const { data } = await api.post('/demo/urls', payload)
        return data
    },

    getUrlStats: async (id: number) => {
        const { data } = await api.get(`/urls/${id}/stats`)
        return data
    }
}
