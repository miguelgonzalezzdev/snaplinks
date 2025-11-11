package com.github.miguelgonzalezzdev.snaplinks.services;

import com.github.miguelgonzalezzdev.snaplinks.dtos.LoginRequest;
import com.github.miguelgonzalezzdev.snaplinks.dtos.RegisterRequest;
import com.github.miguelgonzalezzdev.snaplinks.dtos.TokenResponse;
import com.github.miguelgonzalezzdev.snaplinks.models.Token;
import com.github.miguelgonzalezzdev.snaplinks.models.User;
import com.github.miguelgonzalezzdev.snaplinks.repositories.TokenRepository;
import com.github.miguelgonzalezzdev.snaplinks.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private TokenRepository tokenRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .id(UUID.randomUUID())
                .name("User")
                .email("user@test.com")
                .password("encodedPass")
                .build();
    }

    // ------------------------------------------------------------
    // REGISTER
    // ------------------------------------------------------------
    @Test
    void register_shouldReturnTokenResponse() {
        RegisterRequest request = new RegisterRequest("User", "user@test.com", "1234");

        when(passwordEncoder.encode("1234")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        TokenResponse response = authService.register(request);

        assertThat(response.accessToken()).isEqualTo("accessToken");
        assertThat(response.refreshToken()).isEqualTo("refreshToken");

        verify(userRepository).save(any(User.class));
        verify(tokenRepository).save(any(Token.class));
    }

    // ------------------------------------------------------------
    // LOGIN
    // ------------------------------------------------------------
    @Test
    void login_shouldReturnTokenResponse() {
        LoginRequest request = new LoginRequest("user@test.com", "1234");

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        TokenResponse response = authService.login(request);

        assertThat(response.accessToken()).isEqualTo("accessToken");
        assertThat(response.refreshToken()).isEqualTo("refreshToken");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void login_shouldThrow_whenUserNotFound() {
        LoginRequest request = new LoginRequest("notfound@test.com", "1234");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(NoSuchElementException.class);
    }

    // ------------------------------------------------------------
    // REFRESH TOKEN
    // ------------------------------------------------------------
    @Test
    void refreshToken_shouldReturnNewAccessToken() {
        String header = "Bearer refreshToken123";

        when(jwtService.extractUsername("refreshToken123")).thenReturn("user@test.com");
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid("refreshToken123", user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("newAccessToken");

        TokenResponse response = authService.refreshToken(header);

        assertThat(response.accessToken()).isEqualTo("newAccessToken");
        assertThat(response.refreshToken()).isEqualTo("refreshToken123");
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void refreshToken_shouldThrow_whenHeaderInvalid() {
        assertThatThrownBy(() -> authService.refreshToken(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid Bearer token");

        assertThatThrownBy(() -> authService.refreshToken("Token xyz"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid Bearer token");
    }

    @Test
    void refreshToken_shouldThrow_whenUserNotFound() {
        String header = "Bearer refreshToken123";
        when(jwtService.extractUsername("refreshToken123")).thenReturn("unknown@test.com");
        when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.refreshToken(header))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void refreshToken_shouldThrow_whenTokenInvalid() {
        String header = "Bearer refreshToken123";
        when(jwtService.extractUsername("refreshToken123")).thenReturn("user@test.com");
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid("refreshToken123", user)).thenReturn(false);

        assertThatThrownBy(() -> authService.refreshToken(header))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid Refresh Token");
    }

    // ------------------------------------------------------------
    // REVOKE TOKENS
    // ------------------------------------------------------------
    @Test
    void revokeAllUserTokens_shouldMarkTokensAsRevoked() {
        Token t1 = Token.builder().expired(false).revoked(false).build();
        Token t2 = Token.builder().expired(false).revoked(false).build();
        when(tokenRepository.findAllByExpiredIsFalseOrRevokedIsFalseAndUserId(user.getId()))
                .thenReturn(List.of(t1, t2));

        authService.revokeAllUserTokens(user);

        assertThat(t1.isExpired()).isTrue();
        assertThat(t1.isRevoked()).isTrue();
        verify(tokenRepository).saveAll(anyList());
    }

    @Test
    void revokeAllUserTokens_shouldDoNothing_whenNoTokens() {
        when(tokenRepository.findAllByExpiredIsFalseOrRevokedIsFalseAndUserId(user.getId()))
                .thenReturn(List.of());
        authService.revokeAllUserTokens(user);
        verify(tokenRepository, never()).saveAll(anyList());
    }

    // ------------------------------------------------------------
    // GET AUTHENTICATED USER
    // ------------------------------------------------------------
    @Test
    void getAuthenticatedUser_shouldReturnUser() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("user@test.com");

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        User result = authService.getAuthenticatedUser();
        assertThat(result).isEqualTo(user);
    }

    @Test
    void getAuthenticatedUser_shouldThrow_whenNotAuthenticated() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(false);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        assertThatThrownBy(() -> authService.getAuthenticatedUser())
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Usuario no autenticado");
    }

    @Test
    void getAuthenticatedUser_shouldThrow_whenUserNotFound() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("unknown@test.com");

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.getAuthenticatedUser())
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Usuario no encontrado");
    }
}
