package com.github.miguelgonzalezzdev.snaplinks.controllers;

import com.github.miguelgonzalezzdev.snaplinks.dtos.RedirectResponse;
import com.github.miguelgonzalezzdev.snaplinks.services.RedirectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Redireccionar URLs", description = "Endpoint para redireccionar las URLs acortadas a su URL original.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/u")
public class RedirectController {

    private final RedirectService redirectService;

    @Operation(
        summary = "Redireccionar URL acortada a URL original",
        description = "Recibe un código de URL y redirecciona a la URL original."
    )
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginal(@PathVariable String shortCode, HttpServletRequest request) {
        RedirectResponse url = redirectService.getOriginalUrlByShortCode(shortCode, request);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url.originalUrl())).build();
    }
}
