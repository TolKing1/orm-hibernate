package org.tolking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tolking.dto.LoginDTO;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.criteria.CriteriaTrainerDTO;
import org.tolking.dto.trainer.TrainerCreateDTO;
import org.tolking.dto.trainer.TrainerForTraineeProfileDTO;
import org.tolking.dto.trainer.TrainerNameDTO;
import org.tolking.dto.trainer.TrainerProfileDTO;
import org.tolking.dto.trainer.TrainerUpdateDTO;
import org.tolking.dto.training.TrainingTrainerReadDTO;
import org.tolking.entity.Trainee;
import org.tolking.entity.Trainer;
import org.tolking.entity.TrainingType;
import org.tolking.entity.User;
import org.tolking.enums.RoleType;
import org.tolking.exception.TrainerNotFoundException;
import org.tolking.repository.TrainerRepository;
import org.tolking.service.TrainerService;
import org.tolking.service.TrainingService;
import org.tolking.service.TrainingTypeService;
import org.tolking.service.UserService;
import org.tolking.util.UserUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;

    private final UserService userService;
    private final TrainingTypeService trainingTypeService;
    private final TrainingService trainingService;

    private final PasswordEncoder passwordEncoder;

    private final DTOConverter<Trainer, TrainerCreateDTO> createConverter;
    private final DTOConverter<Trainer, TrainerProfileDTO> profileConverter;
    private final DTOConverter<Trainer, TrainerUpdateDTO> updateConverter;
    private final DTOConverter<Trainer, TrainerForTraineeProfileDTO> traineeProfileConverter;

    @Override
    public LoginDTO create(TrainerCreateDTO dto) {
        log.info("Creating trainer with DTO: {}", dto);
        Trainer trainer = createConverter.toEntity(dto);
        trainer.setTrainingType(getTrainingType(trainer));
        User trainerUser = trainer.getUser();

        String serialUsername = generateUniqueUsername(trainerUser);
        String generatedPassword = prepareUserAndGetPassword(trainerUser, serialUsername);

        Trainer createdTrainer = saveTrainer(trainer);
        User createdTrainerUser = createdTrainer.getUser();

        log.info("Trainer created with username: {}", createdTrainerUser.getUsername());
        return new LoginDTO(createdTrainerUser.getUsername(), generatedPassword);
    }

    private TrainingType getTrainingType(Trainer trainer) {
        TrainingType trainingType = trainingTypeService.findByName(trainer.getTrainingType().getName());
        log.debug("Found training type: {}", trainingType);
        return trainingType;
    }

    private String generateUniqueUsername(User user) {
        String username = userService.getNewUsername(UserUtils.getUsername(user));
        log.debug("Generated unique username: {}", username);
        return username;
    }

    private String prepareUserAndGetPassword(User user, String username) {
        String password = UserUtils.prepareUserForCreation(user, passwordEncoder, username, RoleType.ROLE_TRAINER);
        log.debug("Prepared user with username: {}", username);

        return password;
    }

    private Trainer saveTrainer(Trainer trainer) {
        log.debug("Saving trainer: {}", trainer);
        return trainerRepository.save(trainer);
    }

    @Override
    public TrainerProfileDTO getProfile(String username) throws TrainerNotFoundException {
        log.info("Fetching profile for user: {}", username);
        Trainer trainer = this.getTrainerByUsername(username);
        return profileConverter.toDto(trainer);
    }

    @Override
    public TrainerProfileDTO update(String username, TrainerUpdateDTO trainerUpdateDTO) throws TrainerNotFoundException {
        log.info("Updating trainer profile for user: {}", username);

        Trainer trainer = this.getTrainerByUsername(username);
        trainer = updateConverter.updateEntity(trainer, trainerUpdateDTO);
        Trainer updatedTrainer = trainerRepository.save(trainer);

        log.info("Trainer profile updated for user: {}", username);
        return profileConverter.toDto(updatedTrainer);

    }

    @Override
    public void updatePassword(String username, String newPassword) throws TrainerNotFoundException {
        log.info("Updating password for user: {}", username);

        Trainer trainer = this.getTrainerByUsername(username);
        trainer.getUser().setPassword(passwordEncoder.encode(newPassword));
        trainerRepository.save(trainer);

        log.info("Password updated for user: {}", username);
    }

    @Override
    public void toggleStatus(String username) throws TrainerNotFoundException {
        log.info("Toggling status for user: {}", username);
        Trainer trainer = this.getTrainerByUsername(username);
        User user = trainer.getUser();
        UserUtils.toggleStatus(user);
        trainerRepository.save(trainer);
        log.info("Status toggled for user: {}", username);
    }

    @Override
    public List<TrainingTrainerReadDTO> getTrainingList(String username, CriteriaTrainerDTO criteria) throws TrainerNotFoundException {
        log.info("Fetching training list for user: {} with criteria: {}", username, criteria);
        Trainer trainer = this.getTrainerByUsername(username);
        String givenUsername = trainer.getUser().getUsername();
        return trainingService.getTrainerTrainingListByCriteria(givenUsername, criteria);
    }

    @Override
    public List<TrainerForTraineeProfileDTO> getNotAssignedTrainers(String traineeUsername) {
        log.info("Fetching not assigned trainers for trainee: {}", traineeUsername);
        List<Trainer> trainerList = trainerRepository.findTrainersByUser_IsActiveTrueAndTraineeList_User_UsernameIsNot(traineeUsername);
        return traineeProfileConverter.toDtoList(trainerList);
    }

    @Override
    public List<Trainer> getTrainerListByUsernames(List<TrainerNameDTO> dtoList) throws TrainerNotFoundException {
        log.info("Fetching trainers for usernames: {}", dtoList);
        List<Trainer> trainerList = new ArrayList<>();

        dtoList.forEach(trainerNameDTO -> {
            Trainer trainer = getTrainerByUsername(trainerNameDTO.getUsername());
            trainerList.add(trainer);
        });

        return trainerList;
    }

    @Override
    public Trainer getTrainerByUsername(String username) throws TrainerNotFoundException {
        log.info("Fetching trainer by username: {}", username);
        return trainerRepository.getTrainerByUser_Username(username)
                .orElseThrow(() -> new TrainerNotFoundException(username));
    }

    @Override
    public List<TrainerForTraineeProfileDTO> convertToDTOList(List<Trainer> trainerList) {
        log.debug("Converting trainer list to DTO list");
        return traineeProfileConverter.toDtoList(trainerList);
    }

    @Override
    public void removeTraineeAssociation(Trainee trainee) {
        log.info("Removing trainee association for trainee: {}", trainee);
        List<Trainer> trainerList = trainee.getTrainerList();
        trainerList.forEach(trainer -> {
            List<Trainee> traineeList = trainer.getTraineeList();
            traineeList.remove(trainee);
            trainerRepository.save(trainer);
        });
    }
}