import { describe, it, expect, vi, beforeEach } from "vitest";
import { authService } from "../../services/authService";
import { api } from "../../api/api";
import config from "../../config/config";
import type { AxiosResponse } from "axios";
import type { RegisterRequest, LoginRequest, RefreshTokenRequest } from "../../types";

// 🔧 Mock de la instancia de Axios (api)
vi.mock("../../api/api", () => ({
  api: {
    post: vi.fn(),
  },
}));

describe("authService", () => {
  const mockPost = api.post as unknown as ReturnType<typeof vi.fn>;
  const API_URL = config.API_URL + "/auth";

  beforeEach(() => {
    vi.clearAllMocks();
  });

  const mockAuthResponse = {
    access_token: "ACCESS_TOKEN",
    refresh_token: "REFRESH_TOKEN",
  };

  it("register → llama al endpoint correcto y devuelve access + refresh token", async () => {
    mockPost.mockResolvedValueOnce({
      data: mockAuthResponse,
    } as AxiosResponse);

    const payload: RegisterRequest = {
      name: "User",
      email: "user@test.com",
      password: "123456",
    };

    const result = await authService.register(payload);

    expect(mockPost).toHaveBeenCalledWith(`${API_URL}/register`, payload);
    expect(result).toEqual(mockAuthResponse);
  });

  it("login → llama al endpoint correcto y devuelve access + refresh token", async () => {
    mockPost.mockResolvedValueOnce({
      data: mockAuthResponse,
    } as AxiosResponse);

    const payload: LoginRequest = {
      email: "user@test.com",
      password: "123456",
    };

    const result = await authService.login(payload);

    expect(mockPost).toHaveBeenCalledWith(`${API_URL}/login`, payload);
    expect(result).toEqual(mockAuthResponse);
  });

  it("refreshToken → incluye Authorization: Bearer y devuelve access + refresh token", async () => {
    mockPost.mockResolvedValueOnce({
      data: mockAuthResponse,
    } as AxiosResponse);

    const payload: RefreshTokenRequest = {
      refreshToken: "REFRESH_TOKEN",
    };

    const result = await authService.refreshToken(payload);

    expect(mockPost).toHaveBeenCalledWith(
      `${API_URL}/refresh-token`,
      {},
      {
        headers: {
          Authorization: `Bearer ${payload.refreshToken}`,
        },
      }
    );

    expect(result).toEqual(mockAuthResponse);
  });
});
