package com.github.miguelgonzalezzdev.snaplinks.repositories;

import com.github.miguelgonzalezzdev.snaplinks.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {

        // Limpiar la base de datos antes de cada test
        userRepository.deleteAll();

        // Crear y guardar un usuario de prueba
        savedUser = new User();
        savedUser.setEmail("test@example.com");
        savedUser.setPassword("password123");
        savedUser.setName("Test User");

        userRepository.save(savedUser);
    }

    @Test
    void testFindByEmail_exists() {
        Optional<User> userOpt = userRepository.findByEmail("test@example.com");
        assertTrue(userOpt.isPresent());
        assertEquals(savedUser.getEmail(), userOpt.get().getEmail());
        assertEquals(savedUser.getId(), userOpt.get().getId());
    }

    @Test
    void testFindByEmail_notExists() {
        Optional<User> userOpt = userRepository.findByEmail("noone@example.com");
        assertFalse(userOpt.isPresent());
    }
}
