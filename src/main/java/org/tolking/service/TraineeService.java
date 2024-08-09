package org.tolking.service;

import org.tolking.dto.CriteriaTraineeDTO;
import org.tolking.dto.LoginDTO;
import org.tolking.dto.trainee.TraineeDTO;
import org.tolking.dto.trainee.TraineeProfileDTO;
import org.tolking.dto.trainer.TrainerProfileDTO;
import org.tolking.dto.training.TrainingDTO;
import org.tolking.dto.training.TrainingReadDTO;
import org.tolking.exception.UserNotFoundException;

import java.util.List;

public interface TraineeService {
    void create(TraineeDTO dto);

    TraineeProfileDTO getProfile(LoginDTO dto) throws UserNotFoundException;

    void updatePassword(LoginDTO dto, String newPassword) throws UserNotFoundException;

    void update(LoginDTO loginDTO, TraineeProfileDTO traineeUpdateDTO) throws UserNotFoundException;

    void toggleStatus(LoginDTO loginDTO) throws UserNotFoundException;

    void delete(LoginDTO dto) throws UserNotFoundException;

    void addTraining(LoginDTO loginDTO, TrainingDTO dto);

    List<TrainingReadDTO> getTrainingList(LoginDTO loginDTO, CriteriaTraineeDTO criteria);

    List<TrainerProfileDTO> getNotAssignedTrainers(LoginDTO loginDTO);
}
