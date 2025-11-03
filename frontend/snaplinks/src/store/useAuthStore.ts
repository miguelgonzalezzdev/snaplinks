import { create } from 'zustand'
import { authService } from '../services/authService'
import type { LoginRequest, RegisterRequest } from '../types'

interface OperationResult {
    success: boolean
    message: string
    isValid?: boolean
}

interface AuthState {
    accessToken: string | null
    expiresAt: number | null
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

const ONE_DAY_MS = 24 * 60 * 60 * 1000
const getAccessTokenExpiry = (): number => Date.now() + ONE_DAY_MS

export const useAuthStore = create<AuthState>((set, get) => ({
    accessToken: null,
    expiresAt: null,
    refreshToken: null,
    isAuthenticated: false,
    loading: false,
    error: null,

    // REGISTER
    register: async (payload: RegisterRequest): Promise<OperationResult> => {
        set({ loading: true, error: null })
        try {
            const data = await authService.register(payload)
            const expiresAt = getAccessTokenExpiry()

            set({
                accessToken: data.access_token,
                expiresAt: expiresAt,
                refreshToken: data.refresh_token,
                isAuthenticated: true,
                loading: false,
            })

            localStorage.setItem('accessToken', data.access_token)
            localStorage.setItem('expiresAt', expiresAt.toString())
            localStorage.setItem('refreshToken', data.refresh_token)

            return { success: true, isValid: true, message: 'Registro correcto' }
        } catch (err) {
            const message = err instanceof Error ? err.message : 'Error al registrar usuario'

            set({ 
                error: message, 
                loading: false 
            })

            return { success: false, isValid: false, message }
        }
    },

    // LOGIN
    login: async ({ email, password }: LoginRequest): Promise<OperationResult> => {
        set({ loading: true, error: null })
        try {
            const data = await authService.login({ email, password })
            const expiresAt = getAccessTokenExpiry()

            set({
                accessToken: data.access_token,
                expiresAt: expiresAt,
                refreshToken: data.refresh_token,
                isAuthenticated: true,
                loading: false,
            })

            localStorage.setItem('accessToken', data.access_token)
            localStorage.setItem('expiresAt', expiresAt.toString())
            localStorage.setItem('refreshToken', data.refresh_token)

            return { success: true, isValid: true, message: 'Inicio de sesión correcto' }
        } catch (err) {
            const message = err instanceof Error ? err.message : 'Error al iniciar sesión'

            set({ 
                error: message, 
                loading: false 
            })

            return { success: false, isValid: false, message }
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
            const expiresAt = getAccessTokenExpiry()

            set({
                accessToken: data.access_token,
                expiresAt: expiresAt,
                refreshToken: data.refresh_token,
                isAuthenticated: true,
                loading: false,
            })

            localStorage.setItem('accessToken', data.access_token)
            localStorage.setItem('expiresAt', expiresAt.toString())
            localStorage.setItem('refreshToken', data.refresh_token)

            return { success: true, isValid: true, message: 'Token actualizado correctamente' }
        } catch (err) {
            const message = err instanceof Error ? err.message : 'Error al refrescar token'

            set({
                error: message,
                loading: false
            })

            get().logout()

            return { success: false, isValid: false, message }
        }
    },

    // LOGOUT
    logout: (): OperationResult => {
        localStorage.removeItem('accessToken')
        localStorage.removeItem('expiresAt')
        localStorage.removeItem('refreshToken')

        set({
            accessToken: null,
            expiresAt: null,
            refreshToken: null,
            isAuthenticated: false,
        })

        return { success: true, isValid: false, message: 'Sesión cerrada correctamente' }
    },

    // CARGAR SESION GUARDADA
    loadSession: (): OperationResult => {
        const accessToken = localStorage.getItem('accessToken')
        const expiresAt = localStorage.getItem('expiresAt')
        const refreshToken = localStorage.getItem('refreshToken')

        if (accessToken && refreshToken && expiresAt) {

            const now = Date.now()
            const isValid = now < parseInt(expiresAt)

            set({
                accessToken,
                refreshToken,
                expiresAt: parseInt(expiresAt),
                isAuthenticated: isValid, // Si el token no ha expirado, autenticado
            })

            return { success: true, isValid: isValid, message: isValid ? 'Sesión activa' : 'Sesión expirada' }
        }

        return { success: false, isValid: false, message: 'No hay sesión activa' }
    },
}))
