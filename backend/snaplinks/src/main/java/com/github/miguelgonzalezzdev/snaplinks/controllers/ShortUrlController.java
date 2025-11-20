package com.github.miguelgonzalezzdev.snaplinks.controllers;

import com.github.miguelgonzalezzdev.snaplinks.dtos.ShortUrlRequest;
import com.github.miguelgonzalezzdev.snaplinks.dtos.ShortUrlResponse;
import com.github.miguelgonzalezzdev.snaplinks.dtos.UrlStatsResponse;
import com.github.miguelgonzalezzdev.snaplinks.services.ShortUrlService;
import com.github.miguelgonzalezzdev.snaplinks.services.UrlStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Gestionar URLs del usuario", description = "Endpoint para gestionar las URLs acortadas de un usuario.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/urls")
public class ShortUrlController {

    private final ShortUrlService shortUrlService;
    private final UrlStatsService urlStatsService;

    @Operation(
        summary = "Listar todas las URLs de un usuario",
        description = "Devuelve todas las URLs que tiene un usuario."
    )
    @GetMapping()
    public ResponseEntity<List<ShortUrlResponse>> getAllUrlsByUser() {
        List<ShortUrlResponse> urls = shortUrlService.getAllUrlsByUser();
        return ResponseEntity.ok(urls);
    }

    @Operation(
        summary = "Listar datos de una URL",
        description = "Recibe el ID de una URL y devuelve todos sus datos."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ShortUrlResponse> getUrlById(@PathVariable Long id){
        ShortUrlResponse url = shortUrlService.getUrlById(id);
        return ResponseEntity.ok(url);
    }

    @Operation(
        summary = "Crear una URL",
        description = "Recibe los datos para crear una nueva URL acortada y devuelve sus datos."
    )
    @PostMapping()
    public ResponseEntity<ShortUrlResponse> createShortUrl(@RequestBody ShortUrlRequest request) {
        ShortUrlResponse response = shortUrlService.createShortUrl(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "Actualizar una URL",
        description = "Recibe los datos para actualizar una nueva URL acortada y devuelve sus datos."
    )
    @PutMapping("/{id}")
    public ResponseEntity<ShortUrlResponse> updateShortUrl(@PathVariable Long id, @RequestBody ShortUrlRequest request) {
        ShortUrlResponse response = shortUrlService.updateShortUrl(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "Eliminar una URL",
        description = "Recibe el ID de una URL y devuelve true."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteShortUrl(@PathVariable Long id){
        boolean deleted = shortUrlService.deleteShortUrl(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Obtener estadísticas de una URL",
        description = "Recibe el ID de una URL y devuelve sus estadísticas."
    )
    @GetMapping("/{id}/stats")
    public UrlStatsResponse getUrlStats(@PathVariable Long id) {
        return urlStatsService.getStats(id);
    }
}
