package org.tolking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.criteria.CriteriaTraineeDTO;
import org.tolking.dto.criteria.CriteriaTrainerDTO;
import org.tolking.dto.training.TrainingDTO;
import org.tolking.dto.training.TrainingTraineeReadDTO;
import org.tolking.dto.training.TrainingTrainerReadDTO;
import org.tolking.entity.*;
import org.tolking.exception.TrainerNotFoundException;
import org.tolking.repository.TrainingRepository;
import org.tolking.service.TrainerService;
import org.tolking.service.impl.TrainingServiceImpl;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainerService trainerService;

    @Mock
    private DTOConverter<Training, TrainingTraineeReadDTO> readTraineeConverter;

    @Mock
    private DTOConverter<Training, TrainingTrainerReadDTO> readTrainerConverter;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private CriteriaTraineeDTO criteriaTraineeDTO;
    private CriteriaTrainerDTO criteriaTrainerDTO;
    private Trainee trainee;
    private TrainingDTO trainingDTO;
    private Trainer trainer;
    private Training training;

    @BeforeEach
    void setUp() {
        TrainingType trainingType = new TrainingType("CARDIO");
        training = new Training();
        training.setId(1L);

        trainer = new Trainer();
        trainer.setTrainingType(trainingType);
        User user = new User();
        user.setUsername("Tolek");
        user.setPassword("123");
        user.setFirstName("Tolek");
        user.setLastName("Bek");
        user.setIsActive(true);
        trainer.setUser(user);

        trainee = new Trainee();
        User user1 = new User();
        user1.setUsername("Tolek12");
        user1.setPassword("123");
        user1.setFirstName("Tolek");
        user1.setLastName("Bek");
        user1.setIsActive(true);
        trainee.setUser(user1);

        trainingDTO = new TrainingDTO();
        trainingDTO.setTrainerUsername(trainer.getUser().getUsername());
        trainingDTO.setDuration(0L);

        criteriaTraineeDTO = new CriteriaTraineeDTO();
        criteriaTraineeDTO.setFrom(new Date());
        criteriaTraineeDTO.setTo(new Date());
        criteriaTraineeDTO.setTrainerUsername(trainer.getUser().getUsername());
        criteriaTraineeDTO.setTrainingType(trainingType.getName());


        criteriaTrainerDTO = new CriteriaTrainerDTO();
        criteriaTrainerDTO.setFrom(new Date());
        criteriaTrainerDTO.setTo(new Date());
        criteriaTrainerDTO.setTraineeUsername(trainee.getUser().getUsername());
    }

    @Test
    void getTraineeTrainingListByCriteria_ShouldReturnListOfTraineeReadDTOs() {
        when(trainingRepository.findTraineeTrainingsByCriteria(trainee.getUser().getUsername(), criteriaTraineeDTO.getFrom(), criteriaTraineeDTO.getTo(), trainer.getUser().getUsername(), trainer.getTrainingType().getName()))
                .thenReturn(List.of(training));

        List<TrainingTraineeReadDTO> result = trainingService.getTraineeTrainingListByCriteria(trainee.getUser().getUsername(), criteriaTraineeDTO);

        assertNotNull( result);
        verify(trainingRepository, times(1)).findTraineeTrainingsByCriteria(anyString(), any(), any(), anyString(), any());
    }

    @Test
    void getTrainerTrainingListByCriteria_ShouldReturnListOfTrainerReadDTOs() {
        when(trainingRepository.findTrainerTrainingsByCriteria(anyString(), any(), any(), anyString()))
                .thenReturn(List.of(training));

        List<TrainingTrainerReadDTO> result = trainingService.getTrainerTrainingListByCriteria("trainerUsername", criteriaTrainerDTO);

        assertNotNull(result);
        verify(trainingRepository, times(1)).findTrainerTrainingsByCriteria(anyString(), any(), any(), anyString());
    }

    @Test
    void createTraining_ShouldCreateTrainingSuccessfully() throws TrainerNotFoundException {

        // Arrange
        when(trainerService.getTrainerByUsername(anyString())).thenReturn(trainer);
        when(trainingRepository.save(any(Training.class))).thenReturn(training);

        // Act
        trainingService.createTraining(trainee, trainingDTO);

        // Assert
        verify(trainerService, times(1)).getTrainerByUsername(anyString());
        verify(trainingRepository, times(1)).save(any(Training.class));
    }

    @Test
    void createTraining_ShouldThrowTrainerNotFoundException_WhenTrainerNotFound() {
        when(trainerService.getTrainerByUsername(anyString())).thenReturn(null);

        assertThrows(TrainerNotFoundException.class, () -> trainingService.createTraining(trainee, trainingDTO));

        verify(trainerService, times(1)).getTrainerByUsername(anyString());
        verify(trainingRepository, never()).save(any(Training.class));
    }
}
