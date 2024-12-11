package org.tolking.integration.config;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tolking.entity.Trainee;
import org.tolking.entity.Trainer;
import org.tolking.entity.Training;
import org.tolking.entity.TrainingType;
import org.tolking.entity.User;
import org.tolking.enums.RoleType;
import org.tolking.enums.TrainingsType;
import org.tolking.repository.TraineeRepository;
import org.tolking.repository.TrainerRepository;
import org.tolking.repository.TrainingRepository;
import org.tolking.repository.TrainingTypeRepository;
import org.tolking.repository.UserRepository;

import java.sql.Date;
import java.time.LocalDate;

import static org.tolking.integration.util.GlobalConstants.DEFAULT_TRAINEE_USERNAME;
import static org.tolking.integration.util.GlobalConstants.DEFAULT_TRAINER_USERNAME;

@RequiredArgsConstructor
public class Setup {
    private static final String DEFAULT_PASSWORD = "Password123";
    private static final String DEFAULT_FIRST_NAME = "Test";
    private static final String DEFAULT_LAST_NAME = "Test";
    private static final Date DEFAULT_DATE_OF_BIRTH = Date.valueOf("2000-01-01");
    private static final String DEFAULT_ADDRESS = "Test address";

    private final UserRepository userRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final PasswordEncoder passwordEncoder;

    private static Trainer getTrainer(TrainingType trainingType, User user2) {
        Trainer trainer = new Trainer();
        trainer.setUser(user2);
        trainer.setTrainingType(trainingType);
        return trainer;
    }

    private static Trainee getTrainee(User user1) {
        Trainee trainee = new Trainee();
        trainee.setUser(user1);
        trainee.setDateOfBirth(DEFAULT_DATE_OF_BIRTH);
        trainee.setAddress(DEFAULT_ADDRESS);
        return trainee;
    }

    @Before
    public void setUp() {
        Trainee savedtrainee = saveTrainee();

        TrainingType trainingType = trainingTypeRepository.getTrainingTypeByName(TrainingsType.CARDIO)
                .orElseThrow(() -> new RuntimeException("TrainingType not found"));

        Trainer savedTrainer = getSavedTrainer(trainingType);

        saveTraining(savedTrainer, savedtrainee, trainingType);

    }

    @After
    public void cleanUp() {
        traineeRepository.deleteAll();
        trainerRepository.deleteAll();
        userRepository.deleteAll();
        trainingRepository.deleteAll();
    }

    private void saveTraining(Trainer savedTrainer, Trainee savedtrainee, TrainingType trainingType) {
        Training training = Training.builder()
                .id(1000L)
                .trainingName("Test Training")
                .date(LocalDate.now())
                .duration(60)
                .trainer(savedTrainer)
                .trainee(savedtrainee)
                .trainingType(trainingType)
                .build();

        trainingRepository.save(training);
    }

    private Trainer getSavedTrainer(TrainingType trainingType) {
        User user2 = getUser(DEFAULT_TRAINER_USERNAME, RoleType.ROLE_TRAINER);
        Trainer trainer = getTrainer(trainingType, user2);
        return trainerRepository.save(trainer);
    }

    private Trainee saveTrainee() {
        User user1 = getUser(DEFAULT_TRAINEE_USERNAME, RoleType.ROLE_TRAINEE);
        Trainee trainee = getTrainee(user1);
        return traineeRepository.save(trainee);
    }

    private User getUser(String username, RoleType role) {
        User user1 = new User();
        user1.setUsername(username);
        user1.setFirstName(DEFAULT_FIRST_NAME);
        user1.setLastName(DEFAULT_LAST_NAME);
        user1.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        user1.setRole(role);
        user1.setIsActive(true);
        return user1;
    }


}
