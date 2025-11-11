package com.github.miguelgonzalezzdev.snaplinks.controllers;

import com.github.miguelgonzalezzdev.snaplinks.dtos.LoginRequest;
import com.github.miguelgonzalezzdev.snaplinks.dtos.RegisterRequest;
import com.github.miguelgonzalezzdev.snaplinks.dtos.TokenResponse;
import com.github.miguelgonzalezzdev.snaplinks.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequest request) {
        final TokenResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody LoginRequest request) {
        final TokenResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public TokenResponse refreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String authentication
    ) {
        return authService.refreshToken(authentication);
    }
}
