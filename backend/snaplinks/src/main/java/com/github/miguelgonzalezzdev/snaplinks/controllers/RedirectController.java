package com.github.miguelgonzalezzdev.snaplinks.controllers;

import com.github.miguelgonzalezzdev.snaplinks.dtos.RedirectResponse;
import com.github.miguelgonzalezzdev.snaplinks.services.RedirectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/u") // /snaplinks-api/v1
@RequiredArgsConstructor
public class RedirectController {

    private final RedirectService redirectService;

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginal(@PathVariable String shortCode, HttpServletRequest request) {
        RedirectResponse url = redirectService.getOriginalUrlByShortCode(shortCode, request);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url.originalUrl())).build();
    }
}
