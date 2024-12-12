package org.tolking.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.tolking.dto.LoginDTO;
import org.tolking.entity.User;
import org.tolking.exception.BadLoginException;
import org.tolking.exception.LoginAttemptExceedException;
import org.tolking.exception.UserNotFoundException;
import org.tolking.repository.UserRepository;
import org.tolking.service.BruteForceProtectionService;
import org.tolking.service.JWTService;
import org.tolking.service.impl.UserDetailsServiceImpl;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {
    private LoginDTO loginDTO;
    private User user;
    private String username;
    private String password;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTService jwtService;

    @Mock
    private BruteForceProtectionService bruteForceProtectionService;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setup() {
        username = "testuser";
        password = "password";

        user = new User();
        user.setUsername(username);
        user.setPassword(password);

        loginDTO = new LoginDTO(username, password);
    }

    @Test
    public void signIn_ValidCredentials_Success() throws BadLoginException, LoginAttemptExceedException, UserNotFoundException {

        when(userRepository.findByUsernameAndIsActiveTrue(username)).thenReturn(Optional.of(user));

        when(jwtService.generateToken(user)).thenReturn("mocked-jwt-token");

        String token = userDetailsService.signIn(loginDTO);

        verify(userRepository, times(1)).findByUsernameAndIsActiveTrue(username);
        verify(jwtService, times(1)).generateToken(user);
        verify(bruteForceProtectionService, times(1)).loginSucceeded(username);

        assertEquals("mocked-jwt-token", token);
    }

    @Test
    public void signIn_UserNotFound_ExceptionThrown() {
        when(userRepository.findByUsernameAndIsActiveTrue(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userDetailsService.signIn(loginDTO));

        verify(userRepository, times(1)).findByUsernameAndIsActiveTrue(username);
    }

    @Test
    public void signIn_LoginAttemptExceedExceptionThrown() {
        when(bruteForceProtectionService.isBlocked(username, TimeUnit.MINUTES.toMinutes(5))).thenReturn(true);
        assertThrows(LoginAttemptExceedException.class, () -> userDetailsService.signIn(loginDTO));
    }

    @Test
    public void signIn_BadLoginExceptionThrown() {
        when(bruteForceProtectionService.isBlocked(username, TimeUnit.MINUTES.toMinutes(5))).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(AccountExpiredException.class);

        assertThrows(BadLoginException.class, () -> userDetailsService.signIn(loginDTO));
    }

}
