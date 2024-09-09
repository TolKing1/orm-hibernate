package org.tolking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tolking.service.impl.BruteForceProtectionServiceImpl;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class BruteForceProtectionTest {

    public static final String USERNAME = "testuser";
    public static final long LONG_LOCK_TIME = TimeUnit.MINUTES.toMinutes(5);
    public static final long SHORT_LOCK_TIME = TimeUnit.MILLISECONDS.toMillis(1);

    private BruteForceProtectionServiceImpl bruteForceProtectionService;

    @BeforeEach
    public void setUp() {
        bruteForceProtectionService = new BruteForceProtectionServiceImpl();
    }

    @Test
    public void loginSucceeded_ShouldClearAttemptsAndLock() {
        bruteForceProtectionService.loginFailed(USERNAME);
        bruteForceProtectionService.loginFailed(USERNAME);
        bruteForceProtectionService.loginFailed(USERNAME);

        assertTrue(bruteForceProtectionService.isBlocked(USERNAME,LONG_LOCK_TIME));

        bruteForceProtectionService.loginSucceeded(USERNAME);

        assertFalse(bruteForceProtectionService.isBlocked(USERNAME,LONG_LOCK_TIME));
    }

    @Test
    public void loginFailed_ShouldIncreaseAttempts() {

        assertFalse(bruteForceProtectionService.isBlocked(USERNAME,LONG_LOCK_TIME));

        bruteForceProtectionService.loginFailed(USERNAME);
        assertFalse(bruteForceProtectionService.isBlocked(USERNAME,LONG_LOCK_TIME));

        bruteForceProtectionService.loginFailed(USERNAME);
        assertFalse(bruteForceProtectionService.isBlocked(USERNAME,LONG_LOCK_TIME));

        bruteForceProtectionService.loginFailed(USERNAME);
        assertTrue(bruteForceProtectionService.isBlocked(USERNAME,LONG_LOCK_TIME));
    }

    @Test
    public void isBlocked_ShouldReturnTrueIfUserIsBlocked() {
        bruteForceProtectionService.loginFailed(USERNAME);
        bruteForceProtectionService.loginFailed(USERNAME);
        bruteForceProtectionService.loginFailed(USERNAME);

        assertTrue(bruteForceProtectionService.isBlocked(USERNAME, LONG_LOCK_TIME));
    }

    @Test
    public void isBlocked_ShouldReturnFalseAfterLockExpires() throws InterruptedException {
        bruteForceProtectionService.loginFailed(USERNAME);
        bruteForceProtectionService.loginFailed(USERNAME);
        bruteForceProtectionService.loginFailed(USERNAME);

        assertTrue(bruteForceProtectionService.isBlocked(USERNAME, LONG_LOCK_TIME));

        Thread.sleep(TimeUnit.MILLISECONDS.toMillis(20));

        assertFalse(bruteForceProtectionService.isBlocked(USERNAME, SHORT_LOCK_TIME));
    }
}
