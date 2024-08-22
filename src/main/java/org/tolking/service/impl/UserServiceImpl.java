package org.tolking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tolking.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements org.tolking.service.UserService {
    private final UserRepository userRepository;

    @Override
    public String getNewUsername(String baseUsername){
        log.debug("Finding duplicates for username: {}", baseUsername);

        int serialNumber = 1;
        String username = baseUsername;

        while (userRepository.getUserByUsername(username).isPresent()) {
            username = baseUsername + "_" + serialNumber++;
        }

        return username;
    }
}
