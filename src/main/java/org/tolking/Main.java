package org.tolking;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.tolking.config.HibernateConfig;
import org.tolking.dto.CriteriaTraineeDTO;
import org.tolking.dto.CriteriaTrainerDTO;
import org.tolking.dto.LoginDTO;
import org.tolking.dto.trainee.TraineeDTO;
import org.tolking.dto.trainee.TraineeProfileDTO;
import org.tolking.dto.trainer.TrainerDTO;
import org.tolking.dto.trainer.TrainerIdDTO;
import org.tolking.dto.training.TrainingDTO;
import org.tolking.dto.training.TrainingReadDTO;
import org.tolking.dto.trainingType.TrainingTypeIdDTO;
import org.tolking.dto.user.UserCreateDTO;
import org.tolking.service.TraineeService;
import org.tolking.service.TrainerService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        ApplicationContext context = new AnnotationConfigApplicationContext(HibernateConfig.class);
        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
//
        for (int i = 1; i <= 10; i++) {
            // Create TraineeDTO
            TraineeDTO traineeDTO = new TraineeDTO();
            traineeDTO.setDateOfBirth(new Date());
            traineeDTO.setAddress("1234 Elm Street " + i);

            // Set user information for Trainee
            UserCreateDTO traineeUser = new UserCreateDTO();
            traineeUser.setFirstName("TraineeFirstName" + i);
            traineeUser.setLastName("TraineeLastName" + i);
            traineeUser.setUsername("trainee" + i);
            traineeUser.setPassword("password" + i);
            traineeDTO.setUser(traineeUser);

            // Create a new trainee
            traineeService.create(traineeDTO);

            // Nullify password for trainer reuse
            traineeUser.setPassword(null);
        }

// Create 5 Trainers
        for (int i = 1; i <= 5; i++) {
            // Create TrainerDTO
            TrainerDTO trainerDTO = new TrainerDTO();
            trainerDTO.setTrainingType(new TrainingTypeIdDTO(1));

            // Set user information for Trainer
            UserCreateDTO trainerUser = new UserCreateDTO();
            trainerUser.setFirstName("TrainerFirstName" + i);
            trainerUser.setLastName("TrainerLastName" + i);
            trainerUser.setUsername("trainer");
            trainerUser.setPassword("password" + i);
            trainerDTO.setUser(trainerUser);

            // Create a new trainer
            trainerService.create(trainerDTO);

            // Create TrainingDTO for each trainee
            TrainingDTO trainingDTO = TrainingDTO.builder()
                    .trainerId((long)i)  // Assign to a trainer (1 to 5)
                    .trainingName("Training Session " + i)
                    .trainingTypeId(1)
                    .date(new Date(System.currentTimeMillis() + (i * 1000L)))  // Different date for each
                    .duration(120L)  // duration in minutes
                    .build();

            // Add training to the trainee
            traineeService.addTraining(new LoginDTO("trainee"+i, "password" + i), trainingDTO);

            System.out.println("Trainee " + i + " and their training created successfully");
            System.out.println("Trainer " + i + " created successfully");
        }

        List<TrainerIdDTO> longs = new ArrayList<>();
        longs.add(new TrainerIdDTO(1L));
        longs.add(new TrainerIdDTO(2L));

        TraineeProfileDTO traineeProfileDTO = TraineeProfileDTO.builder()
                .trainerList(longs)
                .build();
        traineeService.update(new LoginDTO("trainee1","password1"), traineeProfileDTO);
        longs.add(new TrainerIdDTO(3L));
        traineeService.update(new LoginDTO("trainee1","password1"), traineeProfileDTO);


        CriteriaTrainerDTO dto = CriteriaTrainerDTO.builder()
                .from(new Date(0))
                .to(new Date(2025, Calendar.APRIL,3))
                .traineeUsername("trainee1")
                .build();
        CriteriaTraineeDTO dto1 = CriteriaTraineeDTO.builder()
                .from(new Date(0))
                .to(new Date(2025, Calendar.APRIL,3))
                .trainerUsername("trainer1")
                .trainingTypeName("CARDIO")
                .build();

        traineeService.delete(new LoginDTO("trainee1","password1"));
        List<TrainingReadDTO> trainingReadDTOS = trainerService.getTrainingList(new LoginDTO("trainer1","password1"), dto);
        List<TrainingReadDTO> trainingReadDTOS1 = traineeService.getTrainingList(new LoginDTO("trainee1","password1"), dto1);
        System.out.println( traineeService.getNotAssignedTrainers(new LoginDTO("trainee"+1, "password" + 1)));
        System.out.println(trainingReadDTOS);
        System.out.println(trainingReadDTOS1);
    }
}