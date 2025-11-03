
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

interface RefreshTokenRequest {
    refreshToken: string
}
