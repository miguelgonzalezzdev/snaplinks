package com.github.miguelgonzalezzdev.snaplinks.dtos;

import jakarta.validation.constraints.NotBlank;

public record ShortUrlRequest(

        String name,

        @NotBlank(message = "La URL es obligatoria")
        String originalUrl) {
}
