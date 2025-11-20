package com.github.miguelgonzalezzdev.snaplinks.controllers;

import com.github.miguelgonzalezzdev.snaplinks.config.JwtAuthFilter;
import com.github.miguelgonzalezzdev.snaplinks.dtos.LoginRequest;
import com.github.miguelgonzalezzdev.snaplinks.dtos.RegisterRequest;
import com.github.miguelgonzalezzdev.snaplinks.dtos.TokenResponse;
import com.github.miguelgonzalezzdev.snaplinks.repositories.UserRepository;
import com.github.miguelgonzalezzdev.snaplinks.services.AuthService;
import com.github.miguelgonzalezzdev.snaplinks.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles("test")
@WebMvcTest(controllers = AuthController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        })
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private TokenResponse tokenResponse;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest("User", "user@example.com", "password123");
        loginRequest = new LoginRequest("user@example.com", "password123");

        tokenResponse = new TokenResponse(
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.accessToken",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.refreshToken"
        );

        Mockito.when(authService.register(Mockito.any(RegisterRequest.class)))
                .thenReturn(tokenResponse);

        Mockito.when(authService.login(Mockito.any(LoginRequest.class)))
                .thenReturn(tokenResponse);
    }

    @Test
    void testRegister_returnsTokenResponse() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.access_token").value(tokenResponse.accessToken()))
                .andExpect(jsonPath("$.refresh_token").value(tokenResponse.refreshToken()));
    }

    @Test
    void testLogin_returnsTokenResponse() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value(tokenResponse.accessToken()))
                .andExpect(jsonPath("$.refresh_token").value(tokenResponse.refreshToken()));
    }
}
