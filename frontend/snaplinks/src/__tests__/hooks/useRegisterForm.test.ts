import { describe, it, expect, vi, beforeEach, type Mock } from "vitest";
import { renderHook, act } from "@testing-library/react";
import { useNavigate } from "react-router";
import { useAuthStore } from "../../store/useAuthStore";
import { useRegisterForm } from "../../hooks/useRegisterForm";

// 🔧 Mocks
vi.mock("../../store/useAuthStore", () => ({
  useAuthStore: vi.fn(),
}));

vi.mock("react-router", () => ({
  useNavigate: vi.fn(),
}));

describe("useRegisterForm hook", () => {
  const mockRegister = vi.fn();
  const mockNavigate = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useAuthStore as unknown as Mock).mockReturnValue({
      register: mockRegister,
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
      preventDefault: () => { },
    } as React.ChangeEvent<HTMLInputElement>;
    callback(event);
  };

  it("debe actualizar los campos de formulario correctamente", () => {
    const { result } = renderHook(() => useRegisterForm());

    act(() => {
      changeInput(result.current.handleName, "User");
      changeInput(result.current.handleEmail, "user@test.com");
      changeInput(result.current.handlePassword, "12345678");
      changeInput(result.current.handleRepeatPassword, "12345678");
    });

    expect(result.current.name).toBe("User");
    expect(result.current.email).toBe("user@test.com");
    expect(result.current.password).toBe("12345678");
    expect(result.current.repeatPassword).toBe("12345678");
  });

  it("debe mostrar error si falta algún campo", async () => {
    const { result } = renderHook(() => useRegisterForm());

    await act(async () => {
      const event = { preventDefault: () => { } } as React.FormEvent<HTMLFormElement>;
      await result.current.handleSubmit(event);
    });

    expect(result.current.error).toBe("Por favor, completa todos los campos");
    expect(result.current.isLoading).toBe(false);
  });

  it("debe mostrar error si la contraseña es demasiado corta", async () => {
    const { result } = renderHook(() => useRegisterForm());

    act(() => {
      changeInput(result.current.handleName, "User");
      changeInput(result.current.handleEmail, "user@test.com");
      changeInput(result.current.handlePassword, "1234");
      changeInput(result.current.handleRepeatPassword, "1234");
    });

    await act(async () => {
      const event = { preventDefault: () => { } } as React.FormEvent<HTMLFormElement>;
      await result.current.handleSubmit(event);
    });

    expect(result.current.error).toBe("La contraseña debe tener al menos 8 caracteres");
    expect(result.current.isLoading).toBe(false);
  });

  it("debe mostrar error si las contraseñas no coinciden", async () => {
    const { result } = renderHook(() => useRegisterForm());

    act(() => {
      changeInput(result.current.handleName, "User");
      changeInput(result.current.handleEmail, "user@test.com");
      changeInput(result.current.handlePassword, "12345678");
      changeInput(result.current.handleRepeatPassword, "87654321");
    });

    await act(async () => {
      const event = { preventDefault: () => { } } as React.FormEvent<HTMLFormElement>;
      await result.current.handleSubmit(event);
    });

    expect(result.current.error).toBe("Las contraseñas no coinciden");
    expect(result.current.isLoading).toBe(false);
  });

  it("debe llamar a register y navegar al dashboard si todo es correcto", async () => {
    mockRegister.mockResolvedValueOnce({ success: true });

    const { result } = renderHook(() => useRegisterForm());

    act(() => {
      changeInput(result.current.handleName, "User");
      changeInput(result.current.handleEmail, "user@test.com");
      changeInput(result.current.handlePassword, "12345678");
      changeInput(result.current.handleRepeatPassword, "12345678");
    });

    await act(async () => {
      const event = { preventDefault: () => { } } as React.FormEvent<HTMLFormElement>;
      await result.current.handleSubmit(event);
    });

    expect(mockRegister).toHaveBeenCalledWith({
      name: "User",
      email: "user@test.com",
      password: "12345678",
    });
    expect(mockNavigate).toHaveBeenCalledWith("/dashboard");
    expect(result.current.error).toBe("");
    expect(result.current.isLoading).toBe(false);
  });

  it("debe mostrar error si register falla", async () => {
    mockRegister.mockResolvedValueOnce({ success: false });

    const { result } = renderHook(() => useRegisterForm());

    act(() => {
      changeInput(result.current.handleName, "User");
      changeInput(result.current.handleEmail, "user@test.com");
      changeInput(result.current.handlePassword, "12345678");
      changeInput(result.current.handleRepeatPassword, "12345678");
    });

    await act(async () => {
      const event = { preventDefault: () => { } } as React.FormEvent<HTMLFormElement>;
      await result.current.handleSubmit(event);
    });

    expect(result.current.error).toBe("Error al registrar usuario");
    expect(result.current.isLoading).toBe(false);
  });
});
