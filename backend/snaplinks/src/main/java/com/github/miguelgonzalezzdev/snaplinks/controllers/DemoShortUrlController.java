package com.github.miguelgonzalezzdev.snaplinks.controllers;

import com.github.miguelgonzalezzdev.snaplinks.dtos.DemoShortUrlRequest;
import com.github.miguelgonzalezzdev.snaplinks.dtos.ShortUrlResponse;
import com.github.miguelgonzalezzdev.snaplinks.services.ShortUrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Crear demos URLs acortadas", description = "Endpoint para crear demos de URLs acortadas.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/demo/urls")
public class DemoShortUrlController {

    private final ShortUrlService shortUrlService;

    @Operation(
        summary = "Crear demo URL",
        description = "Crea una URL corta de demostración (con tiempo de vida limitado)."
    )
    @PostMapping
    public ResponseEntity<ShortUrlResponse> createDemoShortUrl(@RequestBody DemoShortUrlRequest request) {
        ShortUrlResponse response = shortUrlService.createDemoShortUrl(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
