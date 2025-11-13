import { beforeEach, describe, expect, it, vi } from "vitest";
import { renderHook, waitFor } from "@testing-library/react";
import { urlService } from "../../services/urlService";
import { useUrlStats } from "../../hooks/useUrlStats";
import type { UrlStats } from "../../types";

vi.mock("../../services/urlService", () => ({
    urlService: {
        getUrlStats: vi.fn(),
    },
}));

describe("useUrlStats", () => {
    const mockGetUrlStats = urlService.getUrlStats as unknown as ReturnType<typeof vi.fn>;

    beforeEach(() => {
        vi.clearAllMocks();
    });

    it("devuelve estadísticas correctamente", async () => {
        const fakeData: UrlStats = {
            id: 1,
            shortCode: "abc123",
            createdAt: "2025-11-13",
            totalAccesses: 100,
            lastAccess: "2025-11-13T10:00:00Z",
            accessesByCountry: { US: 50, ES: 50 },
            accessesByBrowser: { Chrome: 70, Firefox: 30 },
            accessesByDeviceType: { Desktop: 80, Mobile: 20 },
        };

        mockGetUrlStats.mockResolvedValueOnce(fakeData);

        const { result } = renderHook(() => useUrlStats({ id: 1 }));

        expect(result.current.isLoading).toBe(true);

        await waitFor(() => {
            expect(result.current.isLoading).toBe(false);
        });

        expect(result.current.stats).toEqual(fakeData);
        expect(result.current.error).toBe("");
    });
});
