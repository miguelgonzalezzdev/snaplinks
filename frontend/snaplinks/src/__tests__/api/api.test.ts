import { describe, it, expect, vi, beforeEach, type Mock } from "vitest";
import type { AxiosResponse, InternalAxiosRequestConfig } from "axios";
import { api } from "../../api/api";
import { useAuthStore } from "../../store/useAuthStore";

// Mock de Zustand store
vi.mock("../../store/useAuthStore", () => ({
    useAuthStore: {
        getState: vi.fn(),
    },
}));

describe("api (axios instance)", () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it("añade el token de acceso al header Authorization si existe", async () => {
        const mockToken = "123abc";
        (useAuthStore.getState as unknown as Mock).mockReturnValue({ accessToken: mockToken });

        type RequestFulfilled = (config: InternalAxiosRequestConfig) => Promise<InternalAxiosRequestConfig>;

        const interceptorHandlers = (api.interceptors.request as unknown as {
            handlers: Array<{ fulfilled: RequestFulfilled }>;
        }).handlers;

        const requestInterceptor = interceptorHandlers[0].fulfilled;

        const config = await requestInterceptor({ headers: {} } as InternalAxiosRequestConfig);

        expect(config.headers.Authorization).toBe(`Bearer ${mockToken}`);
    });

    it("no añade header Authorization si no hay token", async () => {
        (useAuthStore.getState as unknown as Mock).mockReturnValue({ accessToken: null });

        type RequestFulfilled = (config: InternalAxiosRequestConfig) => Promise<InternalAxiosRequestConfig>;

        const interceptorHandlers = (api.interceptors.request as unknown as {
            handlers: Array<{ fulfilled: RequestFulfilled }>;
        }).handlers;

        const requestInterceptor = interceptorHandlers[0].fulfilled;

        const config = await requestInterceptor({ headers: {} } as InternalAxiosRequestConfig);

        expect(config.headers.Authorization).toBeUndefined();
    });

    it("propaga el error si no es 401", async () => {
        const error = { response: { status: 500 }, config: {} };

        type ResponseRejected = (error: unknown) => Promise<AxiosResponse>;

        const responseHandlers = (api.interceptors.response as unknown as {
            handlers: Array<{ rejected: ResponseRejected }>;
        }).handlers;

        const responseInterceptor = responseHandlers[0].rejected;

        await expect(responseInterceptor(error)).rejects.toEqual(error);
    });

    it("propaga el error si el refresh falla", async () => {
        const mockRefresh = vi.fn().mockRejectedValue(new Error("refresh failed"));
        (useAuthStore.getState as unknown as Mock).mockReturnValue({
            accessToken: "OLD_TOKEN",
            refreshTokenFn: mockRefresh,
        });

        const error = { response: { status: 401 }, config: {} };

        type ResponseRejected = (error: unknown) => Promise<AxiosResponse>;

        const responseHandlers = (api.interceptors.response as unknown as {
            handlers: Array<{ rejected: ResponseRejected }>;
        }).handlers;

        const responseInterceptor = responseHandlers[0].rejected;

        await expect(responseInterceptor(error)).rejects.toThrow("refresh failed");
    });
});
