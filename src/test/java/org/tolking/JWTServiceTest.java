package org.tolking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tolking.entity.User;
import org.tolking.enums.RoleType;
import org.tolking.service.impl.JWTServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JWTServiceTest {
    public static final String USERNAME = "testuser";
    @InjectMocks
    private JWTServiceImpl jwtService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenerateToken() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername(USERNAME);
        mockUser.setRole(RoleType.ROLE_TRAINEE);

        String token = jwtService.generateToken(mockUser);

        assertNotNull(token);
        System.out.println("Generated Token: " + token);
    }

    @Test
    public void testExtractUserName() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername(USERNAME);
        mockUser.setRole(RoleType.ROLE_TRAINEE);

        String token = jwtService.generateToken(mockUser);

        String extractedUsername = jwtService.extractUserName(token);

        assertEquals(USERNAME, extractedUsername);
    }

    @Test
    public void testIsTokenValid() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername(USERNAME);
        mockUser.setRole(RoleType.ROLE_TRAINEE);

        String token = jwtService.generateToken(mockUser);

        boolean isValid = jwtService.isTokenValid(token, mockUser);

        assertTrue(isValid);
    }

}
