package org.tolking.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class BruteForceProtectionServiceImpl implements org.tolking.service.BruteForceProtectionService {
    private static final int MAX_ATTEMPT = 3;

    private final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private final Map<String, Long> lockCache = new ConcurrentHashMap<>();

    @Override
    public void loginSucceeded(String key) {
        attemptsCache.remove(key);
        lockCache.remove(key);
        log.debug("Refresh attempts for username: {}", key);
    }

    @Override
    public void loginFailed(String key) {
        log.debug("Updating attempts for username: {}", key);
        int attempts = attemptsCache.getOrDefault(key, 0);
        attempts++;
        attemptsCache.put(key, attempts);
        if (attempts >= MAX_ATTEMPT) {
            lockCache.put(key, System.currentTimeMillis());
        }
    }

    @Override
    public boolean isBlocked(String key, long staticLockTime) {
        if (!lockCache.containsKey(key)) {
            return false;
        }

        long lockTime = lockCache.get(key);
        if (System.currentTimeMillis() - lockTime > staticLockTime) {
            lockCache.remove(key);
            return false;
        }

        log.debug("User login temporarily blocked for username: {}", key);
        return true;
    }

}
