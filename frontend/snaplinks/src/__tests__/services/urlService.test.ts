// src/__tests__/services/urlService.test.ts
import { describe, it, expect, vi, beforeEach } from "vitest";
import { urlService } from "../../services/urlService";
import { api } from "../../api/api";
import type { AxiosResponse } from "axios";
import type { CreateUrlRequest, UpdateUrlRequest, CreateDemoUrlRequest } from "../../types";

vi.mock("../../api/api", () => ({
    api: {
        get: vi.fn(),
        post: vi.fn(),
        put: vi.fn(),
        delete: vi.fn(),
    },
}));

describe("urlService", () => {
    const mockGet = api.get as unknown as ReturnType<typeof vi.fn>;
    const mockPost = api.post as unknown as ReturnType<typeof vi.fn>;
    const mockPut = api.put as unknown as ReturnType<typeof vi.fn>;
    const mockDelete = api.delete as unknown as ReturnType<typeof vi.fn>;

    beforeEach(() => {
        vi.clearAllMocks();
    });

    const fakeUrl = {
        id: 8,
        name: "Github",
        shortCode: "lryipCJJZo",
        originalUrl: "https://github.com",
        expiresAt: "2025-11-30T00:00:00",
        qrCode:
            "QR_CODE_DATA",
    };

    it("getUserUrls → llama a /urls y devuelve datos", async () => {
        mockGet.mockResolvedValueOnce({ data: [fakeUrl] } as AxiosResponse);

        const result = await urlService.getUserUrls();

        expect(mockGet).toHaveBeenCalledWith("/urls");
        expect(result).toEqual([fakeUrl]);
    });

    it("createUrl → llama a /urls con payload y devuelve el URL creado", async () => {
        const payload: CreateUrlRequest = { name: "Mi web", originalUrl: "https://miweb.com" };
        mockPost.mockResolvedValueOnce({ data: fakeUrl } as AxiosResponse);

        const result = await urlService.createUrl(payload);

        expect(mockPost).toHaveBeenCalledWith("/urls", payload);
        expect(result).toEqual(fakeUrl);
    });

    it("updateUrl → llama a /urls/:id con payload y devuelve URL actualizado", async () => {
        const payload: UpdateUrlRequest = { id: 8, name: "Mi web actualizada", originalUrl: "https://minuevaweb.com" };
        const updatedUrl = { ...fakeUrl, ...payload };
        mockPut.mockResolvedValueOnce({ data: updatedUrl } as AxiosResponse);

        const result = await urlService.updateUrl(payload);

        expect(mockPut).toHaveBeenCalledWith(`/urls/${payload.id}`, {
            name: payload.name,
            originalUrl: payload.originalUrl,
        });
        expect(result).toEqual(updatedUrl);
    });

    it("deleteUrl → llama a /urls/:id y devuelve undefined", async () => {
        mockDelete.mockResolvedValueOnce({ data: undefined } as AxiosResponse);

        const result = await urlService.deleteUrl(8);

        expect(mockDelete).toHaveBeenCalledWith("/urls/8");
        expect(result).toBeUndefined();
    });

    it("createDemoUrl → llama a /demo/urls y devuelve URL demo", async () => {
        const payload: CreateDemoUrlRequest = { originalUrl: "https://demo.com" };
        const demoUrl = { ...fakeUrl, id: 9, originalUrl: payload.originalUrl, shortCode: "demo" };
        mockPost.mockResolvedValueOnce({ data: demoUrl } as AxiosResponse);

        const result = await urlService.createDemoUrl(payload);

        expect(mockPost).toHaveBeenCalledWith("/demo/urls", payload);
        expect(result).toEqual(demoUrl);
    });

    it("getUrlStats → llama a /urls/:id/stats y devuelve estadísticas", async () => {
        const stats = { clicks: 100, uniqueVisitors: 50 };
        mockGet.mockResolvedValueOnce({ data: stats } as AxiosResponse);

        const result = await urlService.getUrlStats(8);

        expect(mockGet).toHaveBeenCalledWith("/urls/8/stats");
        expect(result).toEqual(stats);
    });
});
