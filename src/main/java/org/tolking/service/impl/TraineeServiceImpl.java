package org.tolking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.tolking.dao.TraineeDAO;
import org.tolking.dto.CriteriaTraineeDTO;
import org.tolking.dto.LoginDTO;
import org.tolking.dto.trainer.TrainerProfileDTO;
import org.tolking.dto.training.TrainingDTO;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.converter.trainee.TraineeProfileConverter;
import org.tolking.dto.trainee.TraineeDTO;
import org.tolking.dto.trainee.TraineeProfileDTO;
import org.tolking.dto.training.TrainingReadDTO;
import org.tolking.entity.Trainee;
import org.tolking.entity.Trainer;
import org.tolking.entity.Training;
import org.tolking.entity.User;
import org.tolking.enums.TrainingsType;
import org.tolking.exception.UserNotFoundException;
import org.tolking.service.TraineeService;
import org.tolking.util.UserUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log
public class TraineeServiceImpl implements TraineeService {
    private final TraineeDAO traineeDAO;
    private final DTOConverter<Trainee, TraineeDTO> createConverter;
    private final DTOConverter<Training, TrainingDTO> trainingConverter;
    private final DTOConverter<Training, TrainingReadDTO> trainingReadConverter;
    private final DTOConverter<Trainer, TrainerProfileDTO> trainerConverter;
    private final TraineeProfileConverter profileConverter;

    @Override
    public void create(TraineeDTO dto) {
        Trainee trainee = createConverter.toEntity(dto);
        User traineeUser = trainee.getUser();
        String serialUsername = traineeDAO.getUsername(traineeUser.getUsername());

        UserUtils.prepareUserForCreation(traineeUser,serialUsername);

        traineeDAO.create(trainee);
        log.info("Trainee has been created with username = %s".formatted(serialUsername));
    }

    @Override
    public TraineeProfileDTO getProfile(LoginDTO dto) throws UserNotFoundException {
        Trainee trainee = getTrainee(dto);

        return profileConverter.toDto(trainee);
    }

    @Override
    public void updatePassword(LoginDTO dto, String newPassword) throws UserNotFoundException {
        Trainee trainee = getTrainee(dto);

        trainee.getUser().setPassword(newPassword);

        traineeDAO.update(trainee);
        log.info("Trainee's password has been updated with id = %s".formatted(trainee.getId()));
    }

    @Override
    public void update(LoginDTO loginDTO, TraineeProfileDTO trainerUpdateDTO) throws UserNotFoundException {
        Trainee trainee = getTrainee(loginDTO);

        trainee = profileConverter.updateEntity(trainee, trainerUpdateDTO);

        traineeDAO.update(trainee);
        log.info("Trainee has been updated with id = %s".formatted(trainee.getId()));
    }

    @Override
    public void toggleStatus(LoginDTO loginDTO) throws UserNotFoundException {
        Trainee trainee = getTrainee(loginDTO);

        traineeDAO.toggleStatus(trainee);
        log.info("Trainee's status has been changed with id = %s".formatted(trainee.getId()));
    }

    @Override
    public void delete(LoginDTO loginDTO) throws UserNotFoundException {
        Trainee trainee = getTrainee(loginDTO);

        traineeDAO.delete(trainee);
        log.info("Trainee has been deleted with id = %s".formatted(trainee.getId()));
    }

    @Override
    public void addTraining(LoginDTO loginDTO, TrainingDTO dto) throws UserNotFoundException {
        Trainee trainee = getTrainee(loginDTO);
        Training training = trainingConverter.toEntity(dto);
        training.setTrainee(trainee);

        trainee.getTrainingList().add(training);

        traineeDAO.update(trainee);
    }

    @Override
    public List<TrainingReadDTO> getTrainingList(LoginDTO loginDTO, CriteriaTraineeDTO criteria) {
        Trainee trainee = getTrainee(loginDTO);
        String username = trainee.getUser().getUsername();
        TrainingsType trainingsType = TrainingsType.valueOf(criteria.getTrainingTypeName());

        List<Training> trainingList = traineeDAO.getTrainingsByCriteria(
                criteria.getFrom(),
                criteria.getTo(),
                criteria.getTrainerUsername(),
                trainingsType, username
        );

        return trainingReadConverter.toDtoList(trainingList);
    }

    @Override
    public List<TrainerProfileDTO> getNotAssignedTrainers(LoginDTO loginDTO) {
        Trainee trainee = getTrainee(loginDTO);
        String username = trainee.getUser().getUsername();
        List<Trainer> trainerList = traineeDAO.getNotAssignedTrainers(username);

        return trainerConverter.toDtoList(trainerList);

    }

    private Trainee getTrainee(LoginDTO dto) throws UserNotFoundException {
        return traineeDAO.getByUsernameAndPassword(dto.getUsername(), dto.getPassword())
                .orElseThrow(()-> {
                    log.warning("Login credentials is incorrect");
                    return new UserNotFoundException(dto.getUsername());
                });
    }
}
