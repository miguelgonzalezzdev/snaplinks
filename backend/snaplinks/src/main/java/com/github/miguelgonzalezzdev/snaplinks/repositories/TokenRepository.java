package com.github.miguelgonzalezzdev.snaplinks.repositories;

import com.github.miguelgonzalezzdev.snaplinks.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, Long> {
    //List<Token> findAllValidIsFalseOrRevokedIsFalseByUserId(UUID userId1);
    List<Token> findAllByExpiredIsFalseOrRevokedIsFalseAndUserId(UUID userId);
    Optional<Token> findByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM tokens t WHERE t.expired = true OR t.revoked = true")
    int deleteExpiredOrRevokedTokens();
}
