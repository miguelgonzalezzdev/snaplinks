import { api } from '../api/api.ts'
import type { CreateUrlRequest } from '../types.ts'

export const urlService = {
    getUserUrls: async () => {
        const { data } = await api.get('/urls')
        return data

    },

    createUrl: async (payload: CreateUrlRequest) => {
        const { data } = await api.post('/urls', payload)
        return data
    },
}
