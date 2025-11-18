package com.github.miguelgonzalezzdev.snaplinks.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "El email es obligatorio")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {
}
