import axios from 'axios'
import { useAuthStore } from '../store/useAuthStore.ts'
import config from "../config/config";

const API_URL = config.API_URL;

export const api = axios.create({
  baseURL: API_URL,
})

// Interceptor para anadir el token de acceso a cada request
api.interceptors.request.use(
  (config) => {

    // Si hay token, se crea el header Authorization
    const { accessToken } = useAuthStore.getState()
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`
    }
    return config
  },

  (error) => Promise.reject(error)
)


// Interceptor para manejar expiracion de tokens (renovar accesos)
api.interceptors.response.use(
  response => response,
  async error => {

    const originalRequest = error.config

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true
      const { refreshTokenFn } = useAuthStore.getState()
      await refreshTokenFn()
      const newToken = useAuthStore.getState().accessToken

      if (newToken) {
        api.defaults.headers.common['Authorization'] = `Bearer ${newToken}`
        originalRequest.headers['Authorization'] = `Bearer ${newToken}`
        return api(originalRequest)
      }
    }

    return Promise.reject(error)
  }
)
