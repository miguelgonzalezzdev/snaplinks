import { api } from '../api/api.ts'
import config from "../config/config";
import type { RegisterRequest, LoginRequest, RefreshTokenRequest } from '../types'

const API_URL = config.API_URL + '/auth';

export const authService = {
    register: async ({ name, email, password }: RegisterRequest) => {
        const { data } = await api.post(API_URL + '/register', { name, email, password })
        return data
    },

    login: async ({ email, password }: LoginRequest) => {
        const { data } = await api.post(API_URL + '/login', { email, password })
        return data
    },

    refreshToken: async ({ refreshToken }: RefreshTokenRequest) => {
        const { data } = await api.post(
            API_URL + '/refresh-token',
            {},      
            {
                headers: {
                    Authorization: `Bearer ${refreshToken}`,
                },
            }
        )

        return data
    },
}
