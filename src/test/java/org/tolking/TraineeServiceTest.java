package org.tolking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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
import org.tolking.service.TrainerService;
import org.tolking.service.TrainingService;
import org.tolking.service.UserService;
import org.tolking.service.impl.TraineeServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {
    public static final LoginDTO loginDTO = new LoginDTO("traineeUsername", "traineePassword");
    @InjectMocks
    private TraineeServiceImpl traineeService;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private UserService userService;

    @Mock
    private TrainingService trainingService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private DTOConverter<Trainee, TraineeCreateDTO> createConverter;

    @Mock
    private DTOConverter<Trainee, TraineeProfileDTO> profileConverter;

    @Mock
    private DTOConverter<Trainee, TraineeUpdateDTO> updateConverter;

    private TraineeCreateDTO traineeCreateDTO;
    private TraineeUpdateDTO traineeUpdateDTO;
    private Trainee trainee;
    private User user;

    @BeforeEach
    public void setUp() {
        traineeCreateDTO = new TraineeCreateDTO();
        traineeUpdateDTO = new TraineeUpdateDTO();

        user = new User();
        user.setUsername("traineeUsername");
        user.setPassword("traineePassword");
        user.setIsActive(true);

        trainee = new Trainee();
        trainee.setUser(user);
    }

    @AfterEach
    public void cleanUp(){
        Mockito.reset(traineeRepository, userService, trainingService, trainerService, createConverter, profileConverter, updateConverter);
    }

    @Test
    public void create_shouldCreateTrainee() {
        when(createConverter.toEntity(any())).thenReturn(trainee);
        when(userService.getNewUsername(anyString())).thenReturn("uniqueUsername");
        when(traineeRepository.save(trainee)).thenReturn(trainee);

        LoginDTO loginDTO = traineeService.create(traineeCreateDTO);

        assertNotNull(loginDTO);
        assertEquals("uniqueUsername", loginDTO.getUsername());
        assertEquals("traineePassword", loginDTO.getPassword());
        verify(createConverter, times(1)).toEntity(traineeCreateDTO);
        verify(userService, times(1)).getNewUsername(anyString());
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    public void getProfile_shouldReturnTraineeProfile_whenTraineeExists() throws UserNotFoundException {
        
        when(traineeRepository.getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.of(trainee));

        traineeService.getProfile(loginDTO);

        verify(traineeRepository, times(1))
                .getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword());
    }

    @Test
    public void getProfile_shouldThrowUserNotFoundException_whenTraineeDoesNotExist() {
        
        when(traineeRepository.getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> traineeService.getProfile(loginDTO));
        verify(traineeRepository, times(1))
                .getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword());
    }

    @Test
    public void updatePassword_shouldUpdatePassword_whenTraineeExists() throws UserNotFoundException {
        LoginNewPassword loginNewPassword = new LoginNewPassword("traineeUsername", "traineePassword", "newPassword");
        when(traineeRepository.getTraineeByUser_UsernameAndUser_Password(loginNewPassword.getUsername(), loginNewPassword.getPassword()))
                .thenReturn(Optional.of(trainee));

        traineeService.updatePassword(loginNewPassword);

        assertEquals("newPassword", trainee.getUser().getPassword());
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    public void updatePassword_shouldThrowUserNotFoundException_whenTraineeDoesNotExist() {
        LoginNewPassword loginNewPassword = new LoginNewPassword("traineeUsername", "traineePassword", "newPassword");
        when(traineeRepository.getTraineeByUser_UsernameAndUser_Password(loginNewPassword.getUsername(), loginNewPassword.getPassword()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> traineeService.updatePassword(loginNewPassword));
        verify(traineeRepository, times(1))
                .getTraineeByUser_UsernameAndUser_Password(loginNewPassword.getUsername(), loginNewPassword.getPassword());
    }

    @Test
    public void update_shouldUpdateTraineeProfile_whenTraineeExists() throws UserNotFoundException {
        
        Trainee updatedTrainee = new Trainee();

        when(traineeRepository.getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any())).thenReturn(updatedTrainee);
        when(updateConverter.updateEntity(trainee, traineeUpdateDTO)).thenReturn(updatedTrainee);

        traineeService.update(loginDTO, traineeUpdateDTO);

        verify(traineeRepository, times(1))
                .getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword());
        verify(traineeRepository, times(1)).save(updatedTrainee);
    }

    @Test
    public void update_shouldThrowUserNotFoundException_whenTraineeDoesNotExist() {
        when(traineeRepository.getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> traineeService.update(loginDTO, traineeUpdateDTO));
        verify(traineeRepository, times(1))
                .getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword());
    }

    @Test
    public void toggleStatus_shouldToggleStatus_whenTraineeExists() throws UserNotFoundException {
        when(traineeRepository.getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.of(trainee));

        traineeService.toggleStatus(loginDTO);

        verify(traineeRepository, times(1)).save(trainee);
        verify(traineeRepository, times(1))
                .getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword());
    }

    @Test
    public void toggleStatus_shouldThrowUserNotFoundException_whenTraineeDoesNotExist() {
        
        when(traineeRepository.getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> traineeService.toggleStatus(loginDTO));
        verify(traineeRepository, times(1))
                .getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword());
    }

    @Test
    public void delete_shouldDeleteTrainee_whenTraineeExists() throws UserNotFoundException {
        when(traineeRepository.getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.of(trainee));

        traineeService.delete(loginDTO);

        verify(traineeRepository, times(1)).delete(trainee);
    }

    @Test
    public void delete_shouldThrowUserNotFoundException_whenTraineeDoesNotExist() {
        when(traineeRepository.getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> traineeService.delete(loginDTO));
        verify(traineeRepository, times(1)).getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword());
    }

    @Test
    public void getTrainingList_shouldReturnTrainingList_whenTraineeExists() {
        CriteriaTraineeDTO criteria = new CriteriaTraineeDTO();
        List<TrainingTraineeReadDTO> trainingList = new ArrayList<>();

        when(traineeRepository.getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.of(trainee));
        when(trainingService.getTraineeTrainingListByCriteria(loginDTO.getUsername(), criteria))
                .thenReturn(trainingList);

        List<TrainingTraineeReadDTO> result = traineeService.getTrainingList(loginDTO, criteria);

        assertNotNull(result);
        assertEquals(trainingList, result);
        verify(trainingService, times(1)).getTraineeTrainingListByCriteria(loginDTO.getUsername(), criteria);
    }

    @Test
    public void getTrainingList_shouldThrowUserNotFoundException_whenTraineeDoesNotExist() {
        CriteriaTraineeDTO criteria = new CriteriaTraineeDTO();

        when(traineeRepository.getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> traineeService.getTrainingList(loginDTO, criteria));
        verify(traineeRepository, times(1)).getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword());
    }

    @Test
    public void getNotAssignedTrainers_shouldReturnListOfTrainers_whenTraineeExists() throws UserNotFoundException {
        List<TrainerForTraineeProfileDTO> trainers = new ArrayList<>();

        when(traineeRepository.getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.of(trainee));
        when(trainerService.getNotAssignedTrainers(loginDTO.getUsername()))
                .thenReturn(trainers);

        List<TrainerForTraineeProfileDTO> result = traineeService.getNotAssignedTrainers(loginDTO);

        assertNotNull(result);
        assertEquals(trainers, result);
        verify(trainerService, times(1)).getNotAssignedTrainers(loginDTO.getUsername());
    }

    @Test
    public void getNotAssignedTrainers_shouldThrowUserNotFoundException_whenTraineeDoesNotExist() {
        when(traineeRepository.getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> traineeService.getNotAssignedTrainers(loginDTO));
        verify(traineeRepository, times(1)).getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword());
    }

    @Test
    public void updateTrainerList_shouldUpdateTrainerList_whenTraineeExists() throws TrainerNotFoundException {
        List<TrainerNameDTO> trainerNameDTOList = new ArrayList<>();
        List<Trainer> trainers = new ArrayList<>();

        when(traineeRepository.getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.of(trainee));
        when(trainerService.getTrainerListByUsernames(trainerNameDTOList)).thenReturn(trainers);
        when(trainerService.convertToDTOList(trainers)).thenReturn(new ArrayList<>());
        when(traineeRepository.save(trainee)).thenReturn(trainee);

        List<TrainerForTraineeProfileDTO> result = traineeService.updateTrainerList(loginDTO, trainerNameDTOList);

        assertNotNull(result);
        verify(trainerService, times(1)).getTrainerListByUsernames(trainerNameDTOList);
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    public void updateTrainerList_shouldThrowTrainerNotFoundException_whenTrainersDoNotExist() throws TrainerNotFoundException {
        TrainerNameDTO e1 = new TrainerNameDTO();
        e1.setUsername("rrww");
        List<TrainerNameDTO> trainerNameDTOList = List.of(e1);

        when(traineeRepository.getTraineeByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.of(trainee));
        when(trainerService.getTrainerListByUsernames(trainerNameDTOList)).thenThrow(new TrainerNotFoundException("rrww"));

        assertThrows(TrainerNotFoundException.class, () -> traineeService.updateTrainerList(loginDTO, trainerNameDTOList));
        verify(trainerService, times(1)).getTrainerListByUsernames(trainerNameDTOList);
    }
}
