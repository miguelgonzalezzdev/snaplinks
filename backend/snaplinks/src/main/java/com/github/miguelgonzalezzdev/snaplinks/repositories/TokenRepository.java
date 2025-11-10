package com.github.miguelgonzalezzdev.snaplinks.repositories;

import com.github.miguelgonzalezzdev.snaplinks.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    //List<Token> findAllValidIsFalseOrRevokedIsFalseByUserId(UUID userId1);
    List<Token> findAllByExpiredIsFalseOrRevokedIsFalseAndUserId(UUID userId);
    Optional<Token> findByToken(String token);
}
