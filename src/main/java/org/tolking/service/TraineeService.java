package org.tolking.service;

import org.tolking.dto.LoginDTO;
import org.tolking.dto.trainee.TraineeDTO;
import org.tolking.dto.trainee.TraineeProfileDTO;
import org.tolking.exception.UserNotFoundException;

public interface TraineeService {
    void create(TraineeDTO dto);

    TraineeProfileDTO getProfile(LoginDTO dto) throws UserNotFoundException;

    void updatePassword(LoginDTO dto, String newPassword) throws UserNotFoundException;

    void update(LoginDTO loginDTO, TraineeProfileDTO trainerUpdateDTO) throws UserNotFoundException;

    void toggleStatus(LoginDTO loginDTO) throws UserNotFoundException;

    void delete(LoginDTO dto) throws UserNotFoundException;
}
