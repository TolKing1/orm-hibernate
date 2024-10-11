package org.tolking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tolking.entity.User;
import org.tolking.repository.UserRepository;
import org.tolking.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private static final String BASE_USERNAME = "tolek.bek";

    @Test
    void getNewUsername_ShouldReturnBaseUsernameWhenNoDuplicates() {
        when(userRepository.getUserByUsername(BASE_USERNAME)).thenReturn(Optional.empty());

        String result = userService.getNewUsername(BASE_USERNAME);

        assertEquals(BASE_USERNAME, result);
        verify(userRepository, times(1)).getUserByUsername(BASE_USERNAME);
    }

    @Test
    void getNewUsername_ShouldReturnUniqueUsernameWithSerialWhenDuplicatesExist() {
        when(userRepository.getUserByUsername(BASE_USERNAME)).thenReturn(Optional.of(new User()));
        when(userRepository.getUserByUsername(BASE_USERNAME + "_1")).thenReturn(Optional.empty());

        String result = userService.getNewUsername(BASE_USERNAME);

        assertEquals(BASE_USERNAME + "_1", result);
        verify(userRepository, times(1)).getUserByUsername(BASE_USERNAME);
        verify(userRepository, times(1)).getUserByUsername(BASE_USERNAME + "_1");
    }

}
