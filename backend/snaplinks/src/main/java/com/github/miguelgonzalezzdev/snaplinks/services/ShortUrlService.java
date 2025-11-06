package com.github.miguelgonzalezzdev.snaplinks.services;

import com.github.miguelgonzalezzdev.snaplinks.dtos.DemoShortUrlRequest;
import com.github.miguelgonzalezzdev.snaplinks.dtos.ShortUrlRequest;
import com.github.miguelgonzalezzdev.snaplinks.dtos.ShortUrlResponse;
import com.github.miguelgonzalezzdev.snaplinks.mappers.ShortUrlMapper;
import com.github.miguelgonzalezzdev.snaplinks.models.ShortUrl;
import com.github.miguelgonzalezzdev.snaplinks.models.User;
import com.github.miguelgonzalezzdev.snaplinks.repositories.ShortUrlRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final ShortUrlMapper shortUrlMapper;
    private final AuthService authService;

    @Value("${application.base-url}")
    private String APP_BASE_URL;

    @Transactional(readOnly = true)
    public List<ShortUrlResponse> getAllUrlsByUser() {

        User user = authService.getAuthenticatedUser();

        // Obtener las URLs no expiradas del usuario
        List<ShortUrl> urls = shortUrlRepository
                .findAllByUserIdAndExpiresAtIsNullOrUserIdAndExpiresAtAfter(
                        user.getId(),
                        user.getId(),
                        LocalDateTime.now()
                );

        return shortUrlMapper.toDtoList(urls);
    }

    @Transactional(readOnly = true)
    public ShortUrlResponse getUrlById(Long id) {

        User user = authService.getAuthenticatedUser();

        // Buscar la URL y comprobar que pertenezca al usuario
        Optional<ShortUrl> urlOpt = shortUrlRepository.findByIdAndUserIdAndExpiresAtIsNullOrIdAndUserIdAndExpiresAtAfter(id, user.getId(), id,user.getId(), LocalDateTime.now());

        ShortUrl url = urlOpt.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "URL no encontrada")
        );

        return shortUrlMapper.toDto(url);
    }

    @Transactional
    public ShortUrlResponse createShortUrl(ShortUrlRequest request) {

        User user = authService.getAuthenticatedUser();
        String shortCode = generateUniqueShortCode(request.originalUrl());
        String redirectUrl = APP_BASE_URL + "/u/" + shortCode;
        String qrCode = generateQrCodeBase64FromUrl(redirectUrl,250,250); // small = 128x128

        var url = ShortUrl.builder()
                .name(request.name())
                .shortCode(shortCode)
                .originalUrl(request.originalUrl())
                .qrCode(qrCode)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMonths(1).with(LocalTime.MAX)) // 1 mes de duracion
                .user(user)
                .build();

        var savedUrl = shortUrlRepository.save(url);

        return shortUrlMapper.toDto(savedUrl);
    }

    @Transactional
    public ShortUrlResponse updateShortUrl(Long id, ShortUrlRequest request) {

        User user = authService.getAuthenticatedUser();

        Optional<ShortUrl> urlOpt = shortUrlRepository.findByIdAndUserIdAndExpiresAtIsNullOrIdAndUserIdAndExpiresAtAfter(id, user.getId(), id,user.getId(), LocalDateTime.now());

        ShortUrl url = urlOpt.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "URL no encontrada")
        );

        url.setName(request.name());

        if (!url.getOriginalUrl().equals(request.originalUrl())) {
            url.setOriginalUrl(request.originalUrl());

            // Regenerar QR code para la nueva URL
            String redirectUrl = "https://snaplinks.app/u/" + url.getShortCode();
            String qrCode = generateQrCodeBase64FromUrl(redirectUrl, 250, 250);
            url.setQrCode(qrCode);
        }

        ShortUrl updated = shortUrlRepository.save(url);

        return shortUrlMapper.toDto(updated);
    }

    @Transactional
    public ShortUrlResponse createDemoShortUrl(DemoShortUrlRequest request) {

        String shortCode = generateUniqueShortCode(request.originalUrl());
        String redirectUrl = APP_BASE_URL + "/u/" + shortCode;
        String qrCode = generateQrCodeBase64FromUrl(redirectUrl, 250, 250);

        var url = ShortUrl.builder()
                .name(null)
                .shortCode(shortCode)
                .originalUrl(request.originalUrl())
                .qrCode(qrCode)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(1).with(LocalTime.MAX)) // URLs demo expiran en 1 dia
                .user(null)
                .build();

        var savedUrl = shortUrlRepository.save(url);
        return shortUrlMapper.toDto(savedUrl);
    }

    public boolean deleteShortUrl(Long id){

        User user = authService.getAuthenticatedUser();

        Optional<ShortUrl> shortUrlOption = shortUrlRepository.findById(id);

        if (shortUrlOption.isEmpty()) {
            return false;
        }

        ShortUrl shortUrl = shortUrlOption.get();

        if (!shortUrl.getUser().getId().equals(user.getId())) {
            return false;
        }

        shortUrlRepository.delete(shortUrl);
        return true;
    }

    @Transactional(readOnly = true)
    public ShortUrl findUrlEntityById(Long id) {
        User user = authService.getAuthenticatedUser();
        return shortUrlRepository.findByIdAndUserIdAndExpiresAtIsNullOrIdAndUserIdAndExpiresAtAfter(id, user.getId(), id,user.getId(), LocalDateTime.now())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "URL no encontrada"));
    }

    private String generateShortCodeFromUrl(String url) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(url.getBytes());
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(hash)
                    .substring(0, 10); // usa 8 caracteres
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generando hash", e);
        }
    }

    private String generateRandomShortCode() {

        final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final int CODE_LENGTH = 10;
        final Random RANDOM = new SecureRandom();

        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    public String generateUniqueShortCode(String url) {

        String shortCode = generateShortCodeFromUrl(url);

        Optional<ShortUrl> existing = shortUrlRepository.findByShortCode(shortCode);

        // Si ya existe, generar codigo aleatorio hasta que sea único
        if (existing.isPresent()) {
            String randomCode;
            do {
                randomCode = generateRandomShortCode();
            } while (shortUrlRepository.findByShortCode(randomCode).isPresent());
            return randomCode;
        }

        return shortCode;
    }

    private  String generateQrCodeBase64FromUrl(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

            byte[] pngData = pngOutputStream.toByteArray();

            return Base64.getEncoder().encodeToString(pngData);

        } catch (WriterException | IOException e) {
            throw new RuntimeException("Error generando QR code", e);
        }
    }

}
