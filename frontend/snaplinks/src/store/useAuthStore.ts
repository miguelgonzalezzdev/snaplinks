import { create } from 'zustand'
import { authService } from '../services/authService'
import type { LoginRequest,RegisterRequest } from '../types'

interface OperationResult {
    success: boolean
    message: string
}

interface AuthState {
    accessToken: string | null
    refreshToken: string | null
    isAuthenticated: boolean
    loading: boolean
    error: string | null
    register: (payload: RegisterRequest) => Promise<OperationResult>
    login: (credentials: LoginRequest) => Promise<OperationResult>
    logout: () => OperationResult
    refreshTokenFn: () => Promise<OperationResult>
    loadSession: () => OperationResult
}

export const useAuthStore = create<AuthState>((set, get) => ({
    accessToken: null,
    refreshToken: null,
    isAuthenticated: false,
    loading: false,
    error: null,

    // REGISTER
    register: async (payload: RegisterRequest): Promise<OperationResult> => {
        set({ loading: true, error: null })
        try {
            const data = await authService.register(payload)

            set({
                accessToken: data.access_token,
                refreshToken: data.refresh_token,
                isAuthenticated: true,
                loading: false,
            })

            localStorage.setItem('accessToken', data.access_token)
            localStorage.setItem('refreshToken', data.refresh_token)

            return { success: true, message: 'Registro correcto' }
        } catch (err) {
            const message = err instanceof Error ? err.message : 'Error al registrar usuario'
            set({ error: message, loading: false })
            return { success: false, message }
        }
    },

    // LOGIN
    login: async ({ email, password }: LoginRequest): Promise<OperationResult> => {
        set({ loading: true, error: null })
        try {
            const data = await authService.login({ email, password })

            set({
                accessToken: data.access_token,
                refreshToken: data.refresh_token,
                isAuthenticated: true,
                loading: false,
            })

            localStorage.setItem('accessToken', data.access_token)
            localStorage.setItem('refreshToken', data.refresh_token)

            return { success: true, message: 'Inicio de sesión correcto' }
        } catch (err) {
            const message = err instanceof Error ? err.message : 'Error al iniciar sesión'
            set({ error: message, loading: false })
            return { success: false, message }
        }
    },

    // REFRESH TOKEN
    refreshTokenFn: async (): Promise<OperationResult> => {
        const refreshToken = get().refreshToken || localStorage.getItem('refreshToken')
        if (!refreshToken) {
            return { success: false, message: 'No hay token de refresco disponible' }
        }

        try {
            const data = await authService.refreshToken({ refreshToken: refreshToken })

            set({ accessToken: data.access_token })

            localStorage.setItem('accessToken', data.access_token)

            return { success: true, message: 'Token actualizado correctamente' }
        } catch (err) {
            const message = err instanceof Error ? err.message : 'Error al refrescar token'
            set({ error: message, loading: false })
            get().logout()
            return { success: false, message }
        }
    },

    // LOGOUT
    logout: (): OperationResult => {
        localStorage.removeItem('accessToken')
        localStorage.removeItem('refreshToken')

        set({
            accessToken: null,
            refreshToken: null,
            isAuthenticated: false,
        })
        
        return { success: true, message: 'Sesión cerrada correctamente' }
    },

    // CARGAR SESION GUARDADA
    loadSession: (): OperationResult => {
        const accessToken = localStorage.getItem('accessToken')
        const refreshToken = localStorage.getItem('refreshToken')

        if (accessToken && refreshToken) {
            set({ accessToken, refreshToken })
            return { success: true, message: 'Sesión cargada desde almacenamiento local' }
        }

        return { success: false, message: 'No hay sesión activa' }
    },
}))
