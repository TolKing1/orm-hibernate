package org.tolking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.tolking.dto.LoginDTO;
import org.tolking.exception.BadLoginException;
import org.tolking.exception.LoginAttemptExceedException;
import org.tolking.exception.UserNotFoundException;
import org.tolking.repository.UserRepository;
import org.tolking.service.BruteForceProtectionService;
import org.tolking.service.JWTService;
import org.tolking.service.UserDetailsService;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    public static final long LOCK_TIME = TimeUnit.MINUTES.toMinutes(5);
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final BruteForceProtectionService bruteForceProtectionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {

        return userRepository.findByUsernameAndIsActiveTrue(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public String signIn(LoginDTO loginDTO) throws BadLoginException, LoginAttemptExceedException {
        String username = loginDTO.getUsername();
        log.debug("Try to login with username: {}", username);

        if (bruteForceProtectionService.isBlocked(username, LOCK_TIME)){
            throw new LoginAttemptExceedException();
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    username,
                    loginDTO.getPassword()
            ));
        } catch (AuthenticationException ignored) {
            bruteForceProtectionService.loginFailed(username);

            throw new BadLoginException();
        }

        var user = this.loadUserByUsername(username);

        bruteForceProtectionService.loginSucceeded(username);

        return jwtService.generateToken(user);
    }
}
