package org.tolking.service;

import org.tolking.dto.CriteriaTrainerDTO;
import org.tolking.dto.LoginDTO;
import org.tolking.dto.trainer.TrainerDTO;
import org.tolking.dto.trainer.TrainerProfileDTO;
import org.tolking.dto.trainer.TrainerUpdateDTO;
import org.tolking.dto.training.TrainingReadDTO;
import org.tolking.exception.UserNotFoundException;

import java.util.List;

public interface TrainerService {
    void create(TrainerDTO dto);

    TrainerProfileDTO getProfile(LoginDTO dto) throws UserNotFoundException;

    void updatePassword(LoginDTO dto, String newPassword) throws UserNotFoundException;

    void update(LoginDTO loginDTO, TrainerUpdateDTO trainerUpdateDTO) throws UserNotFoundException;

    void toggleStatus(LoginDTO loginDTO) throws UserNotFoundException;

    List<TrainingReadDTO> getTrainingList(LoginDTO loginDTO, CriteriaTrainerDTO criteria);
}
