package org.tolking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tolking.dto.LoginDTO;
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
import org.tolking.enums.RoleType;
import org.tolking.exception.TraineeNotFoundException;
import org.tolking.exception.TrainerNotFoundException;
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

    private final PasswordEncoder passwordEncoder;

    private final DTOConverter<Trainee, TraineeCreateDTO> createConverter;
    private final DTOConverter<Trainee, TraineeProfileDTO> profileConverter;
    private final DTOConverter<Trainee, TraineeUpdateDTO> updateConverter;

    @Override
    public LoginDTO create(TraineeCreateDTO dto) {
        log.info("Creating trainee with DTO: {}", dto);

        Trainee trainee = createConverter.toEntity(dto);
        User traineeUser = trainee.getUser();

        String serialUsername = generateUniqueUsername(traineeUser);
        String generatedPassword = prepareUser(traineeUser, serialUsername);

        Trainee createdTrainee = saveTrainee(trainee);
        User createdTraineeUser = createdTrainee.getUser();

        log.info("Trainee created with username: {}", createdTraineeUser.getUsername());
        return new LoginDTO(createdTraineeUser.getUsername(), generatedPassword);
    }

    private String generateUniqueUsername(User user) {
        String username = userService.getNewUsername(UserUtils.getUsername(user));

        log.debug("Generated unique username: {}", username);
        return username;
    }

    private String prepareUser(User user, String username) {
        String password = UserUtils.prepareUserForCreation(user, passwordEncoder, username, RoleType.ROLE_TRAINEE);

        log.debug("Prepared user with username: {}", username);

        return password;
    }

    private Trainee saveTrainee(Trainee trainee) {
        log.debug("Saving trainee: {}", trainee);

        return traineeRepository.save(trainee);
    }

    @Override
    public TraineeProfileDTO getProfile(String username) throws TraineeNotFoundException {
        log.info("Fetching profile for user: {}", username);

        Trainee trainee = this.getTraineeByUsername(username);
        return profileConverter.toDto(trainee);
    }

    @Override
    public void updatePassword(String username, String newPassword) throws TraineeNotFoundException {
        log.info("Updating password for user: {}", username);

        Trainee trainee = getTraineeByUsername(username);
        trainee.getUser().setPassword(passwordEncoder.encode(newPassword));
        traineeRepository.save(trainee);

        log.info("Password updated for user: {}", username);
    }


    @Override
    public TraineeProfileDTO update(String username, TraineeUpdateDTO traineeUpdateDTO) throws TraineeNotFoundException {
        log.info("Updating trainee profile for user: {}", username);

        Trainee trainee = this.getTraineeByUsername(username);
        trainee = updateConverter.updateEntity(trainee, traineeUpdateDTO);
        Trainee updatedTrainee = traineeRepository.save(trainee);

        log.info("Trainee updated with username: {}", username);
        return profileConverter.toDto(updatedTrainee);
    }

    @Override
    public void toggleStatus(String username) throws TraineeNotFoundException {
        log.info("Toggling status for user: {}", username);

        Trainee trainee = this.getTraineeByUsername(username);
        User user = trainee.getUser();
        UserUtils.toggleStatus(user);
        traineeRepository.save(trainee);

        log.info("Status toggled for user: {}", username);
    }

    @Override
    public void delete(String username) throws TraineeNotFoundException {
        log.info("Deleting trainee with username: {}", username);

        Trainee trainee = this.getTraineeByUsername(username);
        traineeRepository.delete(trainee);
        log.info("Trainee deleted with username: {}", username);
    }

    @Override
    public List<TrainingTraineeReadDTO> getTrainingList(String username, CriteriaTraineeDTO criteria) throws TraineeNotFoundException {
        log.info("Fetching training list for user: {} with criteria: {}", username, criteria);

        Trainee trainee = this.getTraineeByUsername(username);
        String newUsername = trainee.getUser().getUsername();
        return trainingService.getTraineeTrainingListByCriteria(newUsername, criteria);
    }

    @Override
    public List<TrainerForTraineeProfileDTO> getNotAssignedTrainers(String username) throws TraineeNotFoundException {
        log.info("Fetching not assigned trainers for user: {}", username);
        Trainee trainee = this.getTraineeByUsername(username);
        return trainerService.getNotAssignedTrainers(trainee.getUser().getUsername());
    }

    @Override
    public List<TrainerForTraineeProfileDTO> updateTrainerList(String username, List<TrainerNameDTO> trainerNameDTOList) throws TrainerNotFoundException, TraineeNotFoundException, IllegalArgumentException {
        log.info("Updating trainer list for user: {}", username);

        Trainee trainee = setTrainerList(username, trainerNameDTOList);
        Trainee updatedTrainee = traineeRepository.save(trainee);

        log.info("Trainer list updated for user: {}", username);
        return trainerService.convertToDTOList(updatedTrainee.getTrainerList());
    }

    @Override
    public Trainee getTraineeByUsername(String username) {
        return traineeRepository.getTraineeByUser_Username(username)
                .orElseThrow(() -> new TraineeNotFoundException(username));
    }

    private Trainee setTrainerList(String username, List<TrainerNameDTO> trainerNameDTOList) throws IllegalArgumentException, TraineeNotFoundException {
        Trainee trainee = this.getTraineeByUsername(username);
        List<Trainer> newTrainerList = trainerService.getTrainerListByUsernames(trainerNameDTOList);

        if (newTrainerList.isEmpty()){
            throw new IllegalArgumentException("Trainer list can't be empty");
        }

        trainerService.removeTraineeAssociation(trainee);
        trainee.setTrainerList(newTrainerList);
        return trainee;
    }
}
