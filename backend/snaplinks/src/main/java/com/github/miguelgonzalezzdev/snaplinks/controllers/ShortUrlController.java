package com.github.miguelgonzalezzdev.snaplinks.controllers;

import com.github.miguelgonzalezzdev.snaplinks.dtos.ShortUrlResponse;
import com.github.miguelgonzalezzdev.snaplinks.mappers.ShortUrlMapper;
import com.github.miguelgonzalezzdev.snaplinks.models.ShortUrl;
import com.github.miguelgonzalezzdev.snaplinks.models.User;
import com.github.miguelgonzalezzdev.snaplinks.repositories.ShortUrlRepository;
import com.github.miguelgonzalezzdev.snaplinks.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/urls") // /snaplinks-api/v1
@RequiredArgsConstructor
public class ShortUrlController {

    private final ShortUrlRepository shortUrlRepository;
    private final UserRepository userRepository;
    private final ShortUrlMapper shortUrlMapper;

    @GetMapping("")
    public ResponseEntity<List<ShortUrlResponse>> getShortUrlsByUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        LocalDateTime now = LocalDateTime.now();

        List<ShortUrl> urls = shortUrlRepository
                .findAllByUserIdAndExpiresAtIsNullOrUserIdAndExpiresAtAfter(
                        user.getId(),
                        user.getId(),
                        now
                );

        return ResponseEntity.ok(shortUrlMapper.toDtoList(urls));
    }
}
