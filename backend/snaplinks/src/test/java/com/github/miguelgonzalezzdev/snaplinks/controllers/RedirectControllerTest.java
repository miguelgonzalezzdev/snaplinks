package com.github.miguelgonzalezzdev.snaplinks.controllers;

import com.github.miguelgonzalezzdev.snaplinks.config.JwtAuthFilter;
import com.github.miguelgonzalezzdev.snaplinks.dtos.RedirectResponse;
import com.github.miguelgonzalezzdev.snaplinks.services.JwtService;
import com.github.miguelgonzalezzdev.snaplinks.services.RedirectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@ActiveProfiles("test")
@WebMvcTest(controllers = RedirectController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        })
@AutoConfigureMockMvc(addFilters = false)
public class RedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RedirectService redirectService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    private final String shortCode = "DykYnCZdZl";
    private final String originalUrl = "https://github.com/";

    @BeforeEach
    void setUp() {
        Mockito.when(redirectService.getOriginalUrlByShortCode(eq(shortCode), any(HttpServletRequest.class)))
                .thenReturn(new RedirectResponse(originalUrl));
    }

    @Test
    void testRedirectToOriginal_returnsFoundWithLocation() throws Exception {
        mockMvc.perform(get("/u/{shortCode}", shortCode))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(header().string("Location", originalUrl));

        Mockito.verify(redirectService, Mockito.times(1))
                .getOriginalUrlByShortCode(eq(shortCode), any(HttpServletRequest.class));
    }

    @Test
    void testRedirectToOriginal_urlNotFound_returnsNotFound() throws Exception {
        String invalidShortCode = "invalid123";

        Mockito.when(redirectService.getOriginalUrlByShortCode(eq(invalidShortCode), any(HttpServletRequest.class)))
                .thenThrow(new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "URL no encontrada"));

        mockMvc.perform(get("/u/{shortCode}", invalidShortCode))
                .andDo(print())
                .andExpect(status().isNotFound());

        Mockito.verify(redirectService, Mockito.times(1))
                .getOriginalUrlByShortCode(eq(invalidShortCode), any(HttpServletRequest.class));
    }
}
