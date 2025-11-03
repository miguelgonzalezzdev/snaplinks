import { api } from '../api/api.ts'

export const urlService = {
    getUserUrls: async () => {
        const { data } = await api.get('/urls')
        return data

    },
}
