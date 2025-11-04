
export interface User {
    id: string
    name: string
    email: string
}

export type UserId = User["id"]
export type UserName = User["name"]
export type UserEmail = User["email"]

export interface RegisterRequest {
    name: string
    email: string
    password: string
}

export interface LoginRequest {
    email: string
    password: string
}

export interface RefreshTokenRequest {
    refreshToken: string
}

export interface Url {
    id: number
    name: string
    shortCode: string
    originalUrl: string
    expiresAt: string
    qrCode: string
}

export interface CreateUrlRequest {
    name: string
    originalUrl: string
}

export interface UpdateUrlRequest {
    id: number
    name: string
    originalUrl: string
}

