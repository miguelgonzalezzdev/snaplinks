package com.github.miguelgonzalezzdev.snaplinks.controllers;

import com.github.miguelgonzalezzdev.snaplinks.dtos.ShortUrlRequest;
import com.github.miguelgonzalezzdev.snaplinks.dtos.ShortUrlResponse;
import com.github.miguelgonzalezzdev.snaplinks.dtos.UrlStatsResponse;
import com.github.miguelgonzalezzdev.snaplinks.services.ShortUrlService;
import com.github.miguelgonzalezzdev.snaplinks.services.UrlStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/urls") // /snaplinks-api/v1
@RequiredArgsConstructor
public class ShortUrlController {

    private final ShortUrlService shortUrlService;
    private final UrlStatsService urlStatsService;

    @GetMapping()
    public ResponseEntity<List<ShortUrlResponse>> getAllUrlsByUser() {
        List<ShortUrlResponse> urls = shortUrlService.getAllUrlsByUser();
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShortUrlResponse> getUrlById(@PathVariable Long id){
        ShortUrlResponse url = shortUrlService.getUrlById(id);
        return ResponseEntity.ok(url);
    }

    @PostMapping()
    public ResponseEntity<ShortUrlResponse> createShortUrl(@RequestBody ShortUrlRequest request) {
        ShortUrlResponse response = shortUrlService.createShortUrl(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShortUrlResponse> updateShortUrl(@PathVariable Long id, @RequestBody ShortUrlRequest request) {
        ShortUrlResponse response = shortUrlService.updateShortUrl(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteShortUrl(@PathVariable Long id){
        boolean deleted = shortUrlService.deleteShortUrl(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/stats")
    public UrlStatsResponse getUrlStats(@PathVariable Long id) {
        return urlStatsService.getStats(id);
    }
}
