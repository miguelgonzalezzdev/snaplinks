import { describe, it, expect, vi, beforeEach, type Mock } from "vitest";
import { renderHook, act } from "@testing-library/react";
import { useNavigate } from "react-router";
import { useAuthStore } from "../../store/useAuthStore";
import { useLoginForm } from "../../hooks/useLoginForm";

// 🔧 Mocks
vi.mock("../../store/useAuthStore", () => ({
  useAuthStore: vi.fn(),
}));

vi.mock("react-router", () => ({
  useNavigate: vi.fn(),
}));

describe("useLoginForm hook", () => {
  const mockLogin = vi.fn();
  const mockNavigate = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useAuthStore as unknown as Mock).mockReturnValue({
      login: mockLogin,
    });
    (useNavigate as unknown as Mock).mockReturnValue(mockNavigate);
  });

  // Helper para simular cambios en inputs
  const changeInput = (
    callback: (e: React.ChangeEvent<HTMLInputElement>) => void,
    value: string
  ) => {
    const event = { 
        target: { value }, 
        preventDefault: () => {},
    } as React.ChangeEvent<HTMLInputElement>;
    callback(event);
  };

  it("debe actualizar los campos correctamente", () => {
    const { result } = renderHook(() => useLoginForm());

    act(() => {
      changeInput(result.current.handleEmail, "user@test.com");
      changeInput(result.current.handlePassword, "12345678");
    });

    expect(result.current.email).toBe("user@test.com");
    expect(result.current.password).toBe("12345678");
  });

  it("debe mostrar error si falta algún campo", async () => {
    const { result } = renderHook(() => useLoginForm());

    await act(async () => {
      const event = { preventDefault: () => {} } as React.FormEvent<HTMLFormElement>;
      await result.current.handleSubmit(event);
    });

    expect(result.current.error).toBe("Por favor, completa todos los campos");
    expect(result.current.isLoading).toBe(false);
  });

  it("debe llamar a login y navegar al dashboard si los datos son correctos", async () => {
    mockLogin.mockResolvedValueOnce({ success: true });

    const { result } = renderHook(() => useLoginForm());

    act(() => {
      changeInput(result.current.handleEmail, "user@test.com");
      changeInput(result.current.handlePassword, "12345678");
    });

    await act(async () => {
      const event = { preventDefault: () => {} } as React.FormEvent<HTMLFormElement>;
      await result.current.handleSubmit(event);
    });

    expect(mockLogin).toHaveBeenCalledWith({
      email: "user@test.com",
      password: "12345678",
    });
    expect(mockNavigate).toHaveBeenCalledWith("/dashboard");
    expect(result.current.error).toBe("");
    expect(result.current.isLoading).toBe(false);
  });

  it("debe mostrar error si login falla", async () => {
    mockLogin.mockResolvedValueOnce({ success: false });

    const { result } = renderHook(() => useLoginForm());

    act(() => {
      changeInput(result.current.handleEmail, "user@test.com");
      changeInput(result.current.handlePassword, "12345678");
    });

    await act(async () => {
      const event = { preventDefault: () => {} } as React.FormEvent<HTMLFormElement>;
      await result.current.handleSubmit(event);
    });

    expect(result.current.error).toBe("Usuario o contraseña incorrectos");
    expect(result.current.isLoading).toBe(false);
  });
});
