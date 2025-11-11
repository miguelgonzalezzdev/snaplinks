package com.github.miguelgonzalezzdev.snaplinks.controllers;

import com.github.miguelgonzalezzdev.snaplinks.config.JwtAuthFilter;
import com.github.miguelgonzalezzdev.snaplinks.dtos.ShortUrlRequest;
import com.github.miguelgonzalezzdev.snaplinks.dtos.ShortUrlResponse;
import com.github.miguelgonzalezzdev.snaplinks.dtos.UrlStatsResponse;
import com.github.miguelgonzalezzdev.snaplinks.services.JwtService;
import com.github.miguelgonzalezzdev.snaplinks.services.ShortUrlService;
import com.github.miguelgonzalezzdev.snaplinks.services.UrlStatsService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ActiveProfiles("test")
@WebMvcTest(controllers = ShortUrlController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        })
@AutoConfigureMockMvc(addFilters = false)
class ShortUrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ShortUrlService shortUrlService;

    @MockitoBean
    private UrlStatsService urlStatsService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    private ShortUrlRequest request;
    private ShortUrlResponse response;
    private UrlStatsResponse statsResponse;

    @BeforeEach
    void setUp() {
        request = new ShortUrlRequest(
                "Mi URL",
                "https://github.com/"
        );

        response = new ShortUrlResponse(
                1L,
                "Mi URL",
                "shortCode123",
                "https://github.com/",
                LocalDateTime.parse("2025-10-31T23:59:59.999999999"),
                "qrCodeBase64"
        );

        statsResponse = new UrlStatsResponse(
                41L,
                "kE8Apjck0k",
                LocalDateTime.of(2025, 11, 5, 19, 13, 53, 38468),
                6L,
                LocalDateTime.of(2025, 11, 10, 18, 54, 2, 49994),
                Map.of("Desconocido", 5L, "Spain", 1L),
                Map.of("Chrome", 3L, "Desconocido", 3L),
                Map.of("Desktop", 3L, "Desconocido", 3L)
        );

        Mockito.when(shortUrlService.createShortUrl(any(ShortUrlRequest.class))).thenReturn(response);
        Mockito.when(shortUrlService.getAllUrlsByUser()).thenReturn(List.of(response));
        Mockito.when(shortUrlService.getUrlById(1L)).thenReturn(response);
        Mockito.when(shortUrlService.updateShortUrl(eq(1L), any(ShortUrlRequest.class))).thenReturn(response);
        Mockito.when(shortUrlService.deleteShortUrl(1L)).thenReturn(true);
        Mockito.when(urlStatsService.getStats(1L)).thenReturn(statsResponse);
    }

    @Test
    void testGetAllUrls_returnsList() throws Exception {
        mockMvc.perform(get("/urls"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()))
                .andExpect(jsonPath("$[0].name").value(response.name()))
                .andExpect(jsonPath("$[0].shortCode").value(response.shortCode()))
                .andExpect(jsonPath("$[0].originalUrl").value(response.originalUrl()))
                .andExpect(jsonPath("$[0].expiresAt").value(response.expiresAt().toString()))
                .andExpect(jsonPath("$[0].qrCode").value(response.qrCode()));
    }

    @Test
    void testGetUrlById_returnsUrl() throws Exception {
        mockMvc.perform(get("/urls/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.shortCode").value(response.shortCode()))
                .andExpect(jsonPath("$.originalUrl").value(response.originalUrl()))
                .andExpect(jsonPath("$.expiresAt").value(response.expiresAt().toString()))
                .andExpect(jsonPath("$.qrCode").value(response.qrCode()));
    }

    @Test
    void testCreateShortUrl_returnsCreatedUrl() throws Exception {
        mockMvc.perform(post("/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.shortCode").value(response.shortCode()))
                .andExpect(jsonPath("$.originalUrl").value(response.originalUrl()))
                .andExpect(jsonPath("$.expiresAt").value(response.expiresAt().toString()))
                .andExpect(jsonPath("$.qrCode").value(response.qrCode()));
    }

    @Test
    void testUpdateShortUrl_returnsUpdatedUrl() throws Exception {
        mockMvc.perform(put("/urls/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.shortCode").value(response.shortCode()))
                .andExpect(jsonPath("$.originalUrl").value(response.originalUrl()))
                .andExpect(jsonPath("$.expiresAt").value(response.expiresAt().toString()))
                .andExpect(jsonPath("$.qrCode").value(response.qrCode()));
    }

    @Test
    void testDeleteShortUrl_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/urls/{id}", 1L))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetUrlStats_returnsStats() throws Exception {
        mockMvc.perform(get("/urls/{id}/stats", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(statsResponse.id()))
                .andExpect(jsonPath("$.shortCode").value(statsResponse.shortCode()))
                .andExpect(jsonPath("$.totalAccesses").value(statsResponse.totalAccesses()))
                .andExpect(jsonPath("$.lastAccess").value(statsResponse.lastAccess().toString()))
                .andExpect(jsonPath("$.accessesByCountry.Desconocido").value(5))
                .andExpect(jsonPath("$.accessesByCountry.Spain").value(1))
                .andExpect(jsonPath("$.accessesByBrowser.Chrome").value(3))
                .andExpect(jsonPath("$.accessesByBrowser.Desconocido").value(3))
                .andExpect(jsonPath("$.accessesByDeviceType.Desktop").value(3))
                .andExpect(jsonPath("$.accessesByDeviceType.Desconocido").value(3));
    }
}
