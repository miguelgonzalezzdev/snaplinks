
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
    name?: string
    shortCode: string
    originalUrl: string
    expiresAt?: string
    qrCode: string
}

export type UrlId = Url["id"]

export interface CreateUrlRequest {
    name: string
    originalUrl: string
}

export interface UpdateUrlRequest {
    id: number
    name: string
    originalUrl: string
}

export interface CreateDemoUrlRequest {
    originalUrl: string
}

export interface UrlStats {
    id: number;
    shortCode: string;
    createdAt: string;
    totalAccesses: number;
    lastAccess: string;
    accessesByCountry: Record<string, number>;
    accessesByBrowser: Record<string, number>;
    accessesByDeviceType: Record<string, number>;
}
