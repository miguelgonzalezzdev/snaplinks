package com.github.miguelgonzalezzdev.snaplinks.repositories;

import com.github.miguelgonzalezzdev.snaplinks.models.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByShortCode(String shortCode);
    List<ShortUrl> findAllByUserIdAndExpiresAtIsNullOrUserIdAndExpiresAtAfter(UUID userId1, UUID userId2, LocalDateTime now);
    Optional<ShortUrl> findByShortCodeAndExpiresAtIsNullOrShortCodeAndExpiresAtAfter(String shortCode1, String shortCode2, LocalDateTime now);
    Optional<ShortUrl> findByIdAndUserIdAndExpiresAtIsNullOrIdAndUserIdAndExpiresAtAfter(Long id, UUID userId, Long id2, UUID userId2, LocalDateTime now);

    @Modifying
    @Transactional
    @Query("DELETE FROM ShortUrl u WHERE u.expiresAt IS NOT NULL AND u.expiresAt < CURRENT_TIMESTAMP")
    int deleteExpiredUrls();
}
