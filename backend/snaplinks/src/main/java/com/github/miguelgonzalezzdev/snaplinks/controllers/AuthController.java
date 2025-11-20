package com.github.miguelgonzalezzdev.snaplinks.controllers;

import com.github.miguelgonzalezzdev.snaplinks.dtos.LoginRequest;
import com.github.miguelgonzalezzdev.snaplinks.dtos.RegisterRequest;
import com.github.miguelgonzalezzdev.snaplinks.dtos.TokenResponse;
import com.github.miguelgonzalezzdev.snaplinks.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticación de usuarios", description = "Endpoints para gestionar la autenticación de usuarios.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Recibe los datos para registrar un nuevo usuario y devuelve un token de acceso y un token de refresco."
    )
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequest request) {
        final TokenResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "Iniciar sesión",
        description = "Recibe los datos de un usuario registrado y devuelve un token de acceso y un token de refresco."
    )
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody LoginRequest request) {
        final TokenResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Refrescar token",
        description = "Recibe un token de refresco y devuelve nuevos tokens de acceso y refresco."
    )
    @PostMapping("/refresh-token")
    public TokenResponse refreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String authentication
    ) {
        return authService.refreshToken(authentication);
    }
}
