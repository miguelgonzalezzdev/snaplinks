package com.github.miguelgonzalezzdev.snaplinks.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.miguelgonzalezzdev.snaplinks.config.JwtAuthFilter;
import com.github.miguelgonzalezzdev.snaplinks.dtos.DemoShortUrlRequest;
import com.github.miguelgonzalezzdev.snaplinks.dtos.ShortUrlResponse;
import com.github.miguelgonzalezzdev.snaplinks.services.JwtService;
import com.github.miguelgonzalezzdev.snaplinks.services.ShortUrlService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = DemoShortUrlController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        })
@AutoConfigureMockMvc(addFilters = false)
public class DemoShortUrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShortUrlService shortUrlService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private DemoShortUrlRequest request;
    private ShortUrlResponse response;

    @BeforeEach
    void setUp() {
        request = new DemoShortUrlRequest("https://github.com/");

        response = new ShortUrlResponse(
                15L,
                null,
                "DykYnCZdZl",
                "https://github.com/",
                LocalDateTime.parse("2025-10-31T23:59:59.999999999"),
                "iVBORw0KGgoAAAANSUhEUgAAAPoAAAD6AQAAAACgl2eQAAABbElEQVR4Xu2X0QnDMAxEBR4gI3l1j9QBDK7uzk1rU/rdAwuTqMr7UXSS0hi/rcUe2ewAsgPIDiA7gOx/gB6wdHiPuBqvbgD8hl8XToGvoBeAeMdBHE6EKTAecSfrDNT0SwrPFYCbQEBv45vkHAA2PSU3z97+DoCsR81MyyPKO+YEQGDIi71DBrWzA1CdWkRmjYjtnfX3QEaSKWj/hkeBDlok5wDoaUayYgXFQsoSnhEwXuML+aJ9UKz1PRgAqa5CjWXwBlZNOgCpMZZpUHXilzQ9ANw0xD5V5wZUzTHmWzGK9zQNAPQLCoS1Qr3hfErOAsAQwx6E2HDlclnegwMwmx2rJGbvkPECOj8RbwDNwxy9ABnbZ7zOuv0dgI7E0DXJjPk3apsPDgD8JgzFupDyKjkLoCk16q2C5KJ3BDCBmTInmAaaH5DX6XM5GgJwO8uk9in7t70DwJZRmsqR9TIDftgBZAeQHUB2AJkH8AQRDUmgOY1IcAAAAABJRU5ErkJggg=="
        );

        Mockito.when(shortUrlService.createDemoShortUrl(any(DemoShortUrlRequest.class)))
                .thenReturn(response);
    }

    @Test
    void testCreateDemoShortUrl_returnsCreatedResponse() throws Exception {
        mockMvc.perform(post("/demo/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(15))
                .andExpect(jsonPath("$.name").doesNotExist())
                .andExpect(jsonPath("$.shortCode").value("DykYnCZdZl"))
                .andExpect(jsonPath("$.originalUrl").value("https://github.com/"))
                .andExpect(jsonPath("$.expiresAt").value("2025-10-31T23:59:59.999999999"))
                .andExpect(jsonPath("$.qrCode").exists());

        Mockito.verify(shortUrlService, Mockito.times(1))
                .createDemoShortUrl(any(DemoShortUrlRequest.class));
    }
}
