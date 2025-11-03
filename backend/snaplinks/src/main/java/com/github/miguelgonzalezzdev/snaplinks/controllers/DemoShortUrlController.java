package com.github.miguelgonzalezzdev.snaplinks.controllers;

import com.github.miguelgonzalezzdev.snaplinks.dtos.DemoShortUrlRequest;
import com.github.miguelgonzalezzdev.snaplinks.dtos.ShortUrlResponse;
import com.github.miguelgonzalezzdev.snaplinks.services.ShortUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demo/urls")
@RequiredArgsConstructor
public class DemoShortUrlController {

    private final ShortUrlService shortUrlService;

    @PostMapping
    @CrossOrigin
    public ResponseEntity<ShortUrlResponse> createDemoShortUrl(@RequestBody DemoShortUrlRequest request) {
        ShortUrlResponse response = shortUrlService.createDemoShortUrl(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
