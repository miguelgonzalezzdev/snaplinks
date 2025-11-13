import { describe, it, expect, vi, beforeEach } from "vitest";
import { renderHook, act, waitFor } from "@testing-library/react";
import { useUrls } from "../../hooks/useUrls";
import { urlService } from "../../services/urlService";
import type { Url, CreateUrlRequest, UpdateUrlRequest, CreateDemoUrlRequest } from "../../types";

// 🔧 Mock del servicio
vi.mock("../../services/urlService", () => ({
    urlService: {
        getUserUrls: vi.fn(),
        createUrl: vi.fn(),
        updateUrl: vi.fn(),
        deleteUrl: vi.fn(),
        createDemoUrl: vi.fn(),
    },
}));

describe("useUrls hook", () => {
    const mockGetUserUrls = urlService.getUserUrls as unknown as ReturnType<typeof vi.fn>;
    const mockCreateUrl = urlService.createUrl as unknown as ReturnType<typeof vi.fn>;
    const mockUpdateUrl = urlService.updateUrl as unknown as ReturnType<typeof vi.fn>;
    const mockDeleteUrl = urlService.deleteUrl as unknown as ReturnType<typeof vi.fn>;
    const mockCreateDemoUrl = urlService.createDemoUrl as unknown as ReturnType<typeof vi.fn>;

    beforeEach(() => {
        vi.clearAllMocks();
    });

    it("debe cargar URLs correctamente al montar", async () => {
        const fakeUrls: Url[] = [
            { id: 1, shortCode: "abc", originalUrl: "http://test.com", qrCode: "qr1" },
            { id: 2, shortCode: "def", originalUrl: "http://example.com", qrCode: "qr2" },
        ];
        mockGetUserUrls.mockResolvedValueOnce(fakeUrls);

        const { result } = renderHook(() => useUrls());

        expect(result.current.isLoading).toBe(true);

        await waitFor(() => {
            expect(result.current.isLoading).toBe(false);
        });

        expect(result.current.urls).toEqual(fakeUrls);
        expect(result.current.error).toBe("");
    });

    it("debe crear una nueva URL correctamente", async () => {
        mockGetUserUrls.mockResolvedValueOnce([]);

        const { result } = renderHook(() => useUrls());

        await waitFor(() => expect(result.current.isLoading).toBe(false));

        const newUrl: CreateUrlRequest = { name: "New", originalUrl: "http://new.com" };
        const createdUrl: Url = { id: 3, shortCode: "ghi", originalUrl: "http://new.com", qrCode: "qr3" };
        mockCreateUrl.mockResolvedValueOnce(createdUrl);

        let response;
        await act(async () => {
            response = await result.current.createUrl(newUrl);
        });

        expect(response).toEqual({ success: true, message: "URL creada correctamente" });
        expect(result.current.urls[0]).toEqual(createdUrl);
    });

    it("debe actualizar una URL correctamente", async () => {
        const existingUrl: Url = { id: 1, shortCode: "abc", originalUrl: "http://old.com", qrCode: "qr1" };

        mockGetUserUrls.mockResolvedValueOnce([existingUrl]);
        const { result } = renderHook(() => useUrls());
        await waitFor(() => expect(result.current.isLoading).toBe(false));

        const updated: Url = { ...existingUrl, name: "Updated", originalUrl: "http://updated.com" };
        mockUpdateUrl.mockResolvedValueOnce(updated);

        const editReq: UpdateUrlRequest = { id: 1, name: "Updated", originalUrl: "http://updated.com" };
        let response;
        await act(async () => {
            response = await result.current.editUrl(editReq);
        });

        expect(response).toEqual({ success: true, message: "URL actualizada correctamente" });
        expect(result.current.urls[0]).toEqual(updated);
    });

    it("debe eliminar una URL correctamente", async () => {
        const urlToDelete: Url = { id: 1, shortCode: "abc", originalUrl: "http://del.com", qrCode: "qr1" };

        mockGetUserUrls.mockResolvedValueOnce([urlToDelete]);
        const { result } = renderHook(() => useUrls());
        await waitFor(() => expect(result.current.isLoading).toBe(false));

        mockDeleteUrl.mockResolvedValueOnce(undefined);

        let response;
        await act(async () => {
            response = await result.current.deleteUrl(urlToDelete.id);
        });

        expect(response).toEqual({ success: true, message: "URL eliminada correctamente" });
        expect(result.current.urls).toHaveLength(0);
    });


    it("debe crear una demo URL correctamente", async () => {
        mockGetUserUrls.mockResolvedValueOnce([]);

        const { result } = renderHook(() => useUrls());

        await waitFor(() => expect(result.current.isLoading).toBe(false));

        const demoReq: CreateDemoUrlRequest = { originalUrl: "http://demo.com" };
        const demoUrl: Url = { id: 5, shortCode: "demo", originalUrl: "http://demo.com", qrCode: "qr5" };
        mockCreateDemoUrl.mockResolvedValueOnce(demoUrl);

        let response;
        await act(async () => {
            response = await result.current.createDemoUrl(demoReq);
        });

        expect(response).toEqual({ success: true, url: demoUrl, message: "Demo URL creada correctamente" });
        expect(result.current.urls[0]).toEqual(demoUrl);
    });

    it("debe manejar errores correctamente", async () => {
        const { result } = renderHook(() => useUrls());

        mockGetUserUrls.mockRejectedValueOnce(new Error("Fetch error"));

        await act(async () => {
            await result.current.fetchUrls();
        });

        expect(result.current.error).toBe("Fetch error");
        expect(result.current.isLoading).toBe(false);
    });
});
