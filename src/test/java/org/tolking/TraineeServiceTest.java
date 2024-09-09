package org.tolking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import org.tolking.exception.TraineeNotFoundException;
import org.tolking.exception.TrainerNotFoundException;
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
    private PasswordEncoder passwordEncoder;

    @Mock
    private DTOConverter<Trainee, TraineeCreateDTO> createConverter;

    @Mock
    private DTOConverter<Trainee, TraineeProfileDTO> profileConverter;

    @Mock
    private DTOConverter<Trainee, TraineeUpdateDTO> updateConverter;

    private TraineeCreateDTO traineeCreateDTO;
    private TraineeUpdateDTO traineeUpdateDTO;
    private String traineeUsername;
    private String traineePassword;
    private Trainee trainee;
    private User user;

    @BeforeEach
    public void setUp() {
        traineeUsername = "traineeUsername";
        traineePassword = "traineePassword";

        loginDTO = new LoginDTO(traineeUsername, traineePassword);
        traineeCreateDTO = new TraineeCreateDTO();
        traineeUpdateDTO = new TraineeUpdateDTO();

        user = new User();
        user.setUsername(traineeUsername);
        user.setPassword(traineePassword);
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
        String uniqueUsername = "uniqueUsername";
        String uniquePassword = "askdgfdgkdfjgdflgjld";

        when(createConverter.toEntity(traineeCreateDTO)).thenReturn(trainee);
        when(userService.getNewUsername(anyString())).thenReturn(uniqueUsername);
        when(traineeRepository.save(trainee)).thenReturn(trainee);
        when(passwordEncoder.encode(anyString())).thenReturn(uniquePassword);

        LoginDTO loginDTO = traineeService.create(traineeCreateDTO);

        assertNotNull(loginDTO);
        assertEquals(uniqueUsername, loginDTO.getUsername());
        assertEquals(uniquePassword, loginDTO.getPassword());

        verify(createConverter, times(1)).toEntity(traineeCreateDTO);
        verify(userService, times(1)).getNewUsername(anyString());
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    public void getProfile_shouldReturnTraineeProfile_whenTraineeExists(){

        when(traineeRepository.getTraineeByUser_Username(traineeUsername))
                .thenReturn(Optional.of(trainee));

        traineeService.getProfile(traineeUsername);

        verify(traineeRepository, times(1))
                .getTraineeByUser_Username(traineeUsername);
    }

    @Test
    public void getProfile_shouldThrowUserNotFoundException_whenTraineeDoesNotExist() {

        when(traineeRepository.getTraineeByUser_Username(traineeUsername))
                .thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () -> traineeService.getProfile(traineeUsername));
        verify(traineeRepository, times(1))
                .getTraineeByUser_Username(traineeUsername);
    }

    @Test
    public void updatePassword_shouldUpdatePassword_whenTraineeExists(){
        String newPassword = "newPassword";
        String encoded = "asdljwoajdwoawjodlafgsdgsfdgfd";

        when(traineeRepository.getTraineeByUser_Username(traineeUsername))
                .thenReturn(Optional.of(trainee));

        when(passwordEncoder.encode(newPassword)).thenReturn(encoded);

        traineeService.updatePassword(traineeUsername, newPassword);

        assertEquals(encoded, trainee.getUser().getPassword());
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    public void updatePassword_shouldThrowUserNotFoundException_whenTraineeDoesNotExist() {
        String newPassword = "newPassword";

        when(traineeRepository.getTraineeByUser_Username(traineeUsername))
                .thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () -> traineeService.updatePassword(traineeUsername, newPassword));
        verify(traineeRepository, times(1))
                .getTraineeByUser_Username(traineeUsername);
    }

    @Test
    public void update_shouldUpdateTraineeProfile_whenTraineeExists(){

        Trainee updatedTrainee = new Trainee();

        when(traineeRepository.getTraineeByUser_Username(traineeUsername))
                .thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any())).thenReturn(updatedTrainee);
        when(updateConverter.updateEntity(trainee, traineeUpdateDTO)).thenReturn(updatedTrainee);

        traineeService.update(traineeUsername, traineeUpdateDTO);

        verify(traineeRepository, times(1))
                .getTraineeByUser_Username(traineeUsername);
        verify(traineeRepository, times(1)).save(updatedTrainee);
    }

    @Test
    public void update_shouldThrowUserNotFoundException_whenTraineeDoesNotExist() {
        when(traineeRepository.getTraineeByUser_Username(traineeUsername))
                .thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () -> traineeService.update(traineeUsername, traineeUpdateDTO));
        verify(traineeRepository, times(1))
                .getTraineeByUser_Username(traineeUsername);
    }

    @Test
    public void toggleStatus_shouldToggleStatus_whenTraineeExists(){
        when(traineeRepository.getTraineeByUser_Username(traineeUsername))
                .thenReturn(Optional.of(trainee));

        traineeService.toggleStatus(traineeUsername);

        verify(traineeRepository, times(1)).save(trainee);
        verify(traineeRepository, times(1))
                .getTraineeByUser_Username(traineeUsername);
    }

    @Test
    public void toggleStatus_shouldThrowUserNotFoundException_whenTraineeDoesNotExist() {

        when(traineeRepository.getTraineeByUser_Username(traineeUsername))
                .thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () -> traineeService.toggleStatus(traineeUsername));
        verify(traineeRepository, times(1))
                .getTraineeByUser_Username(traineeUsername);
    }

    @Test
    public void delete_shouldDeleteTrainee_whenTraineeExists(){
        when(traineeRepository.getTraineeByUser_Username(traineeUsername))
                .thenReturn(Optional.of(trainee));

        traineeService.delete(traineeUsername);

        verify(traineeRepository, times(1)).delete(trainee);
    }

    @Test
    public void delete_shouldThrowUserNotFoundException_whenTraineeDoesNotExist() {

        assertThrows(TraineeNotFoundException.class, () -> traineeService.delete(traineeUsername));
        verify(traineeRepository, times(1)).getTraineeByUser_Username(traineeUsername);
    }

    @Test
    public void getTrainingList_shouldReturnTrainingList_whenTraineeExists() {
        CriteriaTraineeDTO criteria = new CriteriaTraineeDTO();
        List<TrainingTraineeReadDTO> trainingList = new ArrayList<>();

        when(trainingService.getTraineeTrainingListByCriteria(traineeUsername, criteria))
                .thenReturn(trainingList);
        when(traineeRepository.getTraineeByUser_Username(traineeUsername))
                .thenReturn(Optional.of(trainee));

        List<TrainingTraineeReadDTO> result = traineeService.getTrainingList(traineeUsername, criteria);

        assertNotNull(result);
        assertEquals(trainingList, result);
        verify(trainingService, times(1)).getTraineeTrainingListByCriteria(traineeUsername, criteria);
    }

    @Test
    public void getTrainingList_shouldThrowUserNotFoundException_whenTraineeDoesNotExist() {
        CriteriaTraineeDTO criteria = new CriteriaTraineeDTO();

        when(traineeRepository.getTraineeByUser_Username(traineeUsername))
                .thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () -> traineeService.getTrainingList(traineeUsername, criteria));
        verify(traineeRepository, times(1)).getTraineeByUser_Username(traineeUsername);
    }

    @Test
    public void getNotAssignedTrainers_shouldReturnListOfTrainers_whenTraineeExists(){
        List<TrainerForTraineeProfileDTO> trainers = new ArrayList<>();

        when(traineeRepository.getTraineeByUser_Username(traineeUsername))
                .thenReturn(Optional.of(trainee));
        when(trainerService.getNotAssignedTrainers(traineeUsername))
                .thenReturn(trainers);

        List<TrainerForTraineeProfileDTO> result = traineeService.getNotAssignedTrainers(traineeUsername);

        assertNotNull(result);
        assertEquals(trainers, result);
        verify(trainerService, times(1)).getNotAssignedTrainers(traineeUsername);
    }

    @Test
    public void getNotAssignedTrainers_shouldThrowUserNotFoundException_whenTraineeDoesNotExist() {
        when(traineeRepository.getTraineeByUser_Username(traineeUsername))
                .thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () -> traineeService.getNotAssignedTrainers(traineeUsername));
        verify(traineeRepository, times(1)).getTraineeByUser_Username(traineeUsername);
    }

    @Test
    public void updateTrainerList_shouldThrowTrainerListEmpty_whenTraineeExists() throws TrainerNotFoundException {
        List<TrainerNameDTO> trainerNameDTOList = new ArrayList<>();
        List<Trainer> trainers = new ArrayList<>();

        when(traineeRepository.getTraineeByUser_Username(traineeUsername))
                .thenReturn(Optional.of(trainee));
        when(trainerService.getTrainerListByUsernames(trainerNameDTOList)).thenReturn(trainers);
        when(trainerService.convertToDTOList(trainers)).thenReturn(new ArrayList<>());
        when(traineeRepository.save(trainee)).thenReturn(trainee);


        assertThrows(IllegalArgumentException.class,
                () -> traineeService.updateTrainerList(traineeUsername, trainerNameDTOList));
        verify(trainerService, times(1)).getTrainerListByUsernames(trainerNameDTOList);
    }

    @Test
    public void updateTrainerList_shouldUpdateTrainerList_whenTraineeExists() throws TrainerNotFoundException {
        List<TrainerNameDTO> trainerNameDTOList = List.of(new TrainerNameDTO());
        List<Trainer> trainers = List.of(new Trainer());

        when(traineeRepository.getTraineeByUser_Username(traineeUsername))
                .thenReturn(Optional.of(trainee));
        when(trainerService.getTrainerListByUsernames(trainerNameDTOList)).thenReturn(trainers);
        when(trainerService.convertToDTOList(trainers)).thenReturn(List.of());
        when(traineeRepository.save(trainee)).thenReturn(trainee);

        traineeService.updateTrainerList(traineeUsername, trainerNameDTOList);

        verify(trainerService, times(1)).getTrainerListByUsernames(trainerNameDTOList);
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    public void updateTrainerList_shouldThrowTrainerNotFoundException_whenTrainersDoNotExist() throws TrainerNotFoundException {
        TrainerNameDTO e1 = new TrainerNameDTO();
        e1.setUsername("rrww");
        List<TrainerNameDTO> trainerNameDTOList = List.of(e1);

        when(traineeRepository.getTraineeByUser_Username(traineeUsername))
                .thenReturn(Optional.of(trainee));
        when(trainerService.getTrainerListByUsernames(trainerNameDTOList)).thenThrow(new TrainerNotFoundException("rrww"));

        assertThrows(TrainerNotFoundException.class, () -> traineeService.updateTrainerList(traineeUsername, trainerNameDTOList));
        verify(trainerService, times(1)).getTrainerListByUsernames(trainerNameDTOList);
    }
}