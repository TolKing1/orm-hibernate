package org.tolking;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.tolking.dao.TraineeDAO;
import org.tolking.dto.CriteriaTraineeDTO;
import org.tolking.dto.LoginDTO;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.converter.trainee.TraineeProfileConverter;
import org.tolking.dto.trainee.TraineeDTO;
import org.tolking.dto.trainee.TraineeProfileDTO;
import org.tolking.dto.trainer.TrainerProfileDTO;
import org.tolking.dto.training.TrainingDTO;
import org.tolking.dto.training.TrainingReadDTO;
import org.tolking.entity.*;
import org.tolking.enums.TrainingsType;
import org.tolking.exception.UserNotFoundException;
import org.tolking.service.impl.TraineeServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class TraineeServiceTest {

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private DTOConverter<Trainee, TraineeDTO> createConverter;

    @Mock
    private DTOConverter<Training, TrainingDTO> trainingConverter;

    @Mock
    private DTOConverter<Training, TrainingReadDTO> trainingReadConverter;

    @Mock
    private DTOConverter<Trainer, TrainerProfileDTO> trainerConverter;

    @Mock
    private TraineeProfileConverter profileConverter;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void create_ShouldCreateTrainee() {
        TraineeDTO traineeDTO = new TraineeDTO();
        Trainee trainee = getTrainer();

        when(createConverter.toEntity(traineeDTO)).thenReturn(trainee);
        when(traineeDAO.getUsername(anyString())).thenReturn("username1");

        traineeService.create(traineeDTO);

        verify(traineeDAO, times(1)).create(trainee);
    }

    @Test
    public void getProfile_ShouldReturnProfile_WhenTraineeExists() throws UserNotFoundException {
        LoginDTO loginDTO = getLoginDTO();
        Trainee trainee = getTrainer();
        TraineeProfileDTO profileDTO = new TraineeProfileDTO();

        when(traineeDAO.getByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.of(trainee));
        when(profileConverter.toDto(trainee)).thenReturn(profileDTO);

        TraineeProfileDTO result = traineeService.getProfile(loginDTO);

        assertEquals(profileDTO, result);
        verify(traineeDAO, times(1)).getByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword());
    }

    @Test(expected = UserNotFoundException.class)
    public void getProfile_ShouldThrowException_WhenTraineeNotFound() throws UserNotFoundException {
        LoginDTO loginDTO = getLoginDTO();

        when(traineeDAO.getByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.empty());

        traineeService.getProfile(loginDTO);

    }

    @Test
    public void updatePassword_ShouldUpdatePassword_WhenTraineeExists() throws UserNotFoundException {
        LoginDTO loginDTO = getLoginDTO();
        String newPassword = "newPassword";
        Trainee trainee = getTrainer();
        User user = new User();
        trainee.setUser(user);

        when(traineeDAO.getByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.of(trainee));

        traineeService.updatePassword(loginDTO, newPassword);

        assertEquals(newPassword, user.getPassword());
        verify(traineeDAO, times(1)).update(trainee);
    }
    @Test
    public void update_ShouldUpdateTrainee_WhenTraineeExists() throws UserNotFoundException {
        LoginDTO loginDTO = getLoginDTO();
        TraineeProfileDTO traineeUpdateDTO = new TraineeProfileDTO();
        Trainee trainee = getTrainer();

        when(traineeDAO.getByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.of(trainee));
        when(profileConverter.updateEntity(trainee, traineeUpdateDTO)).thenReturn(trainee);

        traineeService.update(loginDTO, traineeUpdateDTO);

        verify(traineeDAO, times(1)).update(trainee);
    }

    @Test
    public void toggleStatus_ShouldToggleStatus_WhenTraineeExists() throws UserNotFoundException {
        LoginDTO loginDTO = new LoginDTO("username", "password");
        Trainee trainee = getTrainer();

        when(traineeDAO.getByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.of(trainee));

        traineeService.toggleStatus(loginDTO);

        verify(traineeDAO, times(1)).toggleStatus(trainee);
    }

    @Test
    public void delete_ShouldDeleteTrainee_WhenTraineeExists() throws UserNotFoundException {
        LoginDTO loginDTO = getLoginDTO();
        Trainee trainee = getTrainer();

        when(traineeDAO.getByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.of(trainee));

        traineeService.delete(loginDTO);

        verify(traineeDAO, times(1)).delete(trainee);
    }

    @Test
    public void addTraining_ShouldAddTrainingToTrainee() throws UserNotFoundException {
        LoginDTO loginDTO = getLoginDTO();

        TrainingDTO trainingDTO = new TrainingDTO();

        Trainee trainee = getTrainer();

        Training training = new Training();
        training.setTrainee(trainee);

        when(traineeDAO.getByUsernameAndPassword("testuser", "password"))
                .thenReturn(Optional.of(trainee));
        when(trainingConverter.toEntity(trainingDTO)).thenReturn(training);

        traineeService.addTraining(loginDTO, trainingDTO);

        assertTrue(trainee.getTrainingList().contains(training));
        verify(traineeDAO, times(1)).update(trainee);
    }

    @Test
    public void getTrainingList_ShouldReturnTrainingList_WhenTraineeExists() {
        LoginDTO loginDTO = getLoginDTO();
        CriteriaTraineeDTO criteria = new CriteriaTraineeDTO();
        Trainee trainee = getTrainer();
        TrainingsType trainingsType = TrainingsType.CARDIO;
        List<Training> trainingList = new ArrayList<>();
        List<TrainingReadDTO> trainingReadDTOList = new ArrayList<>();

        when(traineeDAO.getByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.of(trainee));
        when(traineeDAO.getTrainingsByCriteria(
                criteria.getFrom(),
                criteria.getTo(),
                criteria.getTrainerUsername(),
                trainingsType,
                trainee.getUser().getUsername()
        )).thenReturn(trainingList);
        when(trainingReadConverter.toDtoList(trainingList)).thenReturn(trainingReadDTOList);

        List<TrainingReadDTO> result = traineeService.getTrainingList(loginDTO, criteria);

        assertEquals(trainingReadDTOList, result);
        verify(traineeDAO, times(1)).getTrainingsByCriteria(
                criteria.getFrom(),
                criteria.getTo(),
                criteria.getTrainerUsername(),
                trainingsType,
                trainee.getUser().getUsername()
        );
    }

    @Test
    public void getNotAssignedTrainers_ShouldReturnListOfNotAssignedTrainers() {
        LoginDTO loginDTO = getLoginDTO();
        Trainee trainee = getTrainer();
        List<Trainer> trainerList = new ArrayList<>();
        List<TrainerProfileDTO> trainerProfileDTOList = new ArrayList<>();

        when(traineeDAO.getByUsernameAndPassword("testuser", "password"))
                .thenReturn(Optional.of(trainee));
        when(traineeDAO.getNotAssignedTrainers("testuser")).thenReturn(trainerList);
        when(trainerConverter.toDtoList(trainerList)).thenReturn(trainerProfileDTOList);

        List<TrainerProfileDTO> result = traineeService.getNotAssignedTrainers(loginDTO);

        assertEquals(trainerProfileDTOList, result);
        verify(traineeDAO, times(1)).getNotAssignedTrainers("testuser");
    }

    private static LoginDTO getLoginDTO() {
        return new LoginDTO("testuser", "password");
    }

    private static Trainee getTrainer() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setIsActive(true);
        user.setFirstName("test");
        user.setLastName("user");

        Trainee trainee = new Trainee();
        trainee.setAddress("sdsd");
        trainee.setUser(user);
        trainee.setDateOfBirth(new Date());
        return trainee;
    }
}