package org.tolking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tolking.dto.LoginDTO;
import org.tolking.dto.LoginNewPassword;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.criteria.CriteriaTraineeDTO;
import org.tolking.dto.trainee.TraineeCreateDTO;
import org.tolking.dto.trainee.TraineeProfileDTO;
import org.tolking.dto.trainee.TraineeUpdateDTO;
import org.tolking.dto.trainer.TrainerForTraineeProfileDTO;
import org.tolking.dto.trainer.TrainerNameDTO;
import org.tolking.dto.training.TrainingTraineeReadDTO;
import org.tolking.entity.Trainee;
import org.tolking.entity.Trainer;
import org.tolking.entity.User;
import org.tolking.exception.TrainerNotFoundException;
import org.tolking.exception.UserNotFoundException;
import org.tolking.repository.TraineeRepository;
import org.tolking.service.TraineeService;
import org.tolking.service.TrainerService;
import org.tolking.service.TrainingService;
import org.tolking.service.UserService;
import org.tolking.util.UserUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TraineeServiceImpl implements TraineeService {
    private final TraineeRepository traineeRepository;
    private final UserService userService;
    private final TrainingService trainingService;
    private final TrainerService trainerService;

    private final DTOConverter<Trainee, TraineeCreateDTO> createConverter;
    private final DTOConverter<Trainee, TraineeProfileDTO> profileConverter;
    private final DTOConverter<Trainee, TraineeUpdateDTO> updateConverter;

    @Override
    public LoginDTO create(TraineeCreateDTO dto) {
        log.info("Creating trainee with DTO: {}", dto);

        Trainee trainee = createConverter.toEntity(dto);
        User traineeUser = trainee.getUser();

        String serialUsername = generateUniqueUsername(traineeUser);
        prepareUser(traineeUser, serialUsername);

        Trainee createdTrainee = saveTrainee(trainee);
        User createdTraineeUser = createdTrainee.getUser();

        log.info("Trainee created with username: {}", createdTraineeUser.getUsername());
        return new LoginDTO(createdTraineeUser.getUsername(), createdTraineeUser.getPassword());
    }

    private String generateUniqueUsername(User user) {
        String username = userService.getNewUsername(UserUtils.getUsername(user));

        log.debug("Generated unique username: {}", username);
        return username;
    }

    private void prepareUser(User user, String username) {
        UserUtils.prepareUserForCreation(user, username);

        log.debug("Prepared user with username: {}", username);
    }

    private Trainee saveTrainee(Trainee trainee) {
        log.debug("Saving trainee: {}", trainee);

        return traineeRepository.save(trainee);
    }

    @Override
    public TraineeProfileDTO getProfile(LoginDTO dto) throws UserNotFoundException {
        log.info("Fetching profile for user: {}", dto.getUsername());

        Trainee trainee = getTraineeByLogin(dto);
        return profileConverter.toDto(trainee);
    }

    @Override
    public void updatePassword(LoginNewPassword dto) throws UserNotFoundException {
        log.info("Updating password for user: {}", dto.getUsername());

        Trainee trainee = getTraineeByLogin(new LoginDTO(dto.getUsername(), dto.getPassword()));
        trainee.getUser().setPassword(dto.getNewPassword());
        traineeRepository.save(trainee);

        log.info("Password updated for user: {}", dto.getUsername());
    }

    @Override
    public TraineeProfileDTO update(LoginDTO loginDTO, TraineeUpdateDTO traineeUpdateDTO) throws UserNotFoundException {
        log.info("Updating trainee profile for user: {}", loginDTO.getUsername());

        Trainee trainee = getTraineeByLogin(loginDTO);
        trainee = updateConverter.updateEntity(trainee, traineeUpdateDTO);
        Trainee updatedTrainee = traineeRepository.save(trainee);

        log.info("Trainee updated with username: {}", loginDTO.getUsername());
        return profileConverter.toDto(updatedTrainee);
    }

    @Override
    public void toggleStatus(LoginDTO loginDTO) throws UserNotFoundException {
        log.info("Toggling status for user: {}", loginDTO.getUsername());

        Trainee trainee = getTraineeByLogin(loginDTO);
        User user = trainee.getUser();
        UserUtils.toggleStatus(user);
        traineeRepository.save(trainee);

        log.info("Status toggled for user: {}", loginDTO.getUsername());
    }

    @Override
    public void delete(LoginDTO loginDTO) throws UserNotFoundException {
        log.info("Deleting trainee with username: {}", loginDTO.getUsername());

        Trainee trainee = getTraineeByLogin(loginDTO);
        traineeRepository.delete(trainee);
        log.info("Trainee deleted with username: {}", loginDTO.getUsername());
    }

    @Override
    public List<TrainingTraineeReadDTO> getTrainingList(LoginDTO loginDTO, CriteriaTraineeDTO criteria) {
        log.info("Fetching training list for user: {} with criteria: {}", loginDTO.getUsername(), criteria);

        Trainee trainee = getTraineeByLogin(loginDTO);
        String username = trainee.getUser().getUsername();
        return trainingService.getTraineeTrainingListByCriteria(username, criteria);
    }

    @Override
    public List<TrainerForTraineeProfileDTO> getNotAssignedTrainers(LoginDTO loginDTO) throws UserNotFoundException {
        log.info("Fetching not assigned trainers for user: {}", loginDTO.getUsername());
        Trainee trainee = getTraineeByLogin(loginDTO);
        return trainerService.getNotAssignedTrainers(trainee.getUser().getUsername());
    }

    @Override
    public List<TrainerForTraineeProfileDTO> updateTrainerList(LoginDTO loginDTO, List<TrainerNameDTO> trainerNameDTOList) throws TrainerNotFoundException {
        log.info("Updating trainer list for user: {}", loginDTO.getUsername());

        Trainee trainee = setTrainerList(loginDTO, trainerNameDTOList);
        Trainee updatedTrainee = traineeRepository.save(trainee);

        log.info("Trainer list updated for user: {}", loginDTO.getUsername());
        return trainerService.convertToDTOList(updatedTrainee.getTrainerList());
    }

    @Override
    public Trainee getTraineeByLogin(LoginDTO dto) throws UserNotFoundException {
        log.info("Fetching trainee by login: {}", dto);
        return traineeRepository.getTraineeByUser_UsernameAndUser_Password(dto.getUsername(), dto.getPassword())
                .orElseThrow(() -> new UserNotFoundException(dto.getUsername()));
    }

    private Trainee setTrainerList(LoginDTO loginDTO, List<TrainerNameDTO> trainerNameDTOList) {
        Trainee trainee = getTraineeByLogin(loginDTO);
        List<Trainer> newTrainerList = trainerService.getTrainerListByUsernames(trainerNameDTOList);
        trainerService.removeTraineeAssociation(trainee);
        trainee.setTrainerList(newTrainerList);
        return trainee;
    }
}
