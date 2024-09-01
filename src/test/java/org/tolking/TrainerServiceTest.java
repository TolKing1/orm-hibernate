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
import org.tolking.dto.criteria.CriteriaTrainerDTO;
import org.tolking.dto.trainer.*;
import org.tolking.dto.training.TrainingTrainerReadDTO;
import org.tolking.entity.Trainee;
import org.tolking.entity.Trainer;
import org.tolking.entity.TrainingType;
import org.tolking.entity.User;
import org.tolking.enums.TrainingsType;
import org.tolking.exception.TrainerNotFoundException;
import org.tolking.exception.UserNotFoundException;
import org.tolking.repository.TrainerRepository;
import org.tolking.service.TrainingService;
import org.tolking.service.TrainingTypeService;
import org.tolking.service.UserService;
import org.tolking.service.impl.TrainerServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserService userService;

    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private TrainingService trainingService;

    @Mock
    private DTOConverter<Trainer, TrainerCreateDTO> createConverter;

    @Mock
    private DTOConverter<Trainer, TrainerProfileDTO> profileConverter;

    @Mock
    private DTOConverter<Trainer, TrainerUpdateDTO> updateConverter;

    @Mock
    private DTOConverter<Trainer, TrainerForTraineeProfileDTO> traineeProfileConverter;

    private TrainerCreateDTO trainerCreateDTO;
    private TrainerUpdateDTO trainerUpdateDTO;
    private TrainingType trainingType;
    private Trainer trainer;
    private Trainee trainee;
    private User user;
    private User user1;

    @BeforeEach
    public void setUp() {
        trainerCreateDTO = new TrainerCreateDTO();
        trainerUpdateDTO = new TrainerUpdateDTO();
        trainingType = new TrainingType("CARDIO");

        trainer = new Trainer();
        trainer.setTrainingType(trainingType);
        user = new User();
        user.setUsername("Tolek");
        user.setPassword("123");
        user.setFirstName("Tolek");
        user.setLastName("Bek");
        user.setIsActive(true);
        trainer.setUser(user);

        trainee = new Trainee();
        user1 = new User();
        user1.setUsername("Tolek12");
        user1.setPassword("123");
        user1.setFirstName("Tolek");
        user1.setLastName("Bek");
        user1.setIsActive(true);
        trainee.setUser(user1);
    }

    @AfterEach
    public void cleanUp(){
        Mockito.reset(trainerRepository, userService,trainingTypeService,trainingService, createConverter, profileConverter, updateConverter );
    }

    @Test
    public void create_shouldCreateTrainer() {
        when(createConverter.toEntity(any(TrainerCreateDTO.class))).thenReturn(trainer);
        when(userService.getNewUsername(anyString())).thenReturn("uniqueUsername");
        when(trainingTypeService.findByName(any(TrainingsType.class))).thenReturn(new TrainingType());
        when(trainerRepository.save(trainer)).thenReturn(trainer);

        trainerService.create(trainerCreateDTO);

        verify(createConverter, times(1)).toEntity(trainerCreateDTO);
        verify(userService, times(1)).getNewUsername(anyString());
        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    public void getProfile_shouldReturnTrainerProfile_whenTrainerExists() throws UserNotFoundException {
        LoginDTO loginDTO = new LoginDTO("username", "password");
        when(trainerRepository.getTrainerByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.of(trainer));
        when(profileConverter.toDto(trainer)).thenReturn(new TrainerProfileDTO());

        trainerService.getProfile(loginDTO);

        verify(trainerRepository, times(1))
                .getTrainerByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword());
        verify(profileConverter, times(1)).toDto(trainer);
    }

    @Test
    public void getProfile_shouldThrowUserNotFoundException_whenTrainerDoesNotExist() {
        LoginDTO loginDTO = new LoginDTO("username", "password");
        when(trainerRepository.getTrainerByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> trainerService.getProfile(loginDTO));
        verify(trainerRepository, times(1))
                .getTrainerByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword());
    }

    @Test
    public void update_shouldUpdateTrainerProfile_whenTrainerExists() throws UserNotFoundException {
        LoginDTO loginDTO = new LoginDTO("username", "password");
        Trainer updatedTrainer = new Trainer();

        when(trainerRepository.getTrainerByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.of(trainer));
        when(updateConverter.updateEntity(trainer, trainerUpdateDTO)).thenReturn(updatedTrainer);
        when(trainerRepository.save(any())).thenReturn(updatedTrainer);

        trainerService.update(loginDTO, trainerUpdateDTO);

        verify(trainerRepository, times(1)).save(updatedTrainer);
    }

    @Test
    public void update_shouldThrowUserNotFoundException_whenTrainerDoesNotExist() {
        LoginDTO loginDTO = new LoginDTO("username", "password");
        when(trainerRepository.getTrainerByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> trainerService.update(loginDTO, trainerUpdateDTO));
        verify(trainerRepository, times(1))
                .getTrainerByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword());
    }

    @Test
    public void updatePassword_shouldUpdatePassword_whenTrainerExists() throws UserNotFoundException {
        LoginNewPassword loginNewPassword = new LoginNewPassword(new LoginDTO("traineeUsername", "traineePassword"), "newPassword");
        LoginDTO loginDTO1 = loginNewPassword.getLoginDTO();
        when(trainerRepository.getTrainerByUser_UsernameAndUser_Password(loginDTO1.getUsername(), loginDTO1.getPassword()))
                .thenReturn(Optional.of(trainer));

        trainerService.updatePassword(loginNewPassword);

        assertEquals("newPassword", trainer.getUser().getPassword());
        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    public void updatePassword_shouldThrowUserNotFoundException_whenTrainerDoesNotExist() {
        LoginNewPassword loginNewPassword = new LoginNewPassword(new LoginDTO("traineeUsername", "traineePassword"), "newPassword");
        LoginDTO loginDTO1 = loginNewPassword.getLoginDTO();
        when(trainerRepository.getTrainerByUser_UsernameAndUser_Password(loginDTO1.getUsername(), loginDTO1.getPassword()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> trainerService.updatePassword(loginNewPassword));
        verify(trainerRepository, times(1))
                .getTrainerByUser_UsernameAndUser_Password(loginDTO1.getUsername(), loginDTO1.getPassword());
    }

    @Test
    public void toggleStatus_shouldToggleStatus_whenTrainerExists() throws UserNotFoundException {
        LoginDTO loginDTO = new LoginDTO("username", "password");
        when(trainerRepository.getTrainerByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.of(trainer));

        trainerService.toggleStatus(loginDTO);

        verify(trainerRepository, times(1)).save(trainer);
        verify(trainerRepository, times(1))
                .getTrainerByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword());
    }

    @Test
    public void toggleStatus_shouldThrowUserNotFoundException_whenTrainerDoesNotExist() {
        LoginDTO loginDTO = new LoginDTO("username", "password");
        when(trainerRepository.getTrainerByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> trainerService.toggleStatus(loginDTO));
        verify(trainerRepository, times(1))
                .getTrainerByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword());
    }

    @Test
    public void getTrainingList_shouldReturnTrainingList_whenTrainerExists() {
        LoginDTO loginDTO = new LoginDTO("username", "password");
        CriteriaTrainerDTO criteria = new CriteriaTrainerDTO(); // Initialize fields as needed
        when(trainerRepository.getTrainerByUser_UsernameAndUser_Password(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(Optional.of(trainer));
        when(trainingService.getTrainerTrainingListByCriteria(anyString(), eq(criteria)))
                .thenReturn(List.of(new TrainingTrainerReadDTO()));

        List<TrainingTrainerReadDTO> result = trainerService.getTrainingList(loginDTO, criteria);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(trainingService, times(1)).getTrainerTrainingListByCriteria(anyString(), eq(criteria));
    }

    @Test
    public void getNotAssignedTrainers_shouldReturnTrainerList() throws UserNotFoundException {
        TrainerForTraineeProfileDTO trainerDTO = new TrainerForTraineeProfileDTO();
        trainerDTO.setUserUsername(user.getUsername());

        List<Trainer> trainerList = List.of(trainer);

        when(trainerRepository.findTrainersByUser_IsActiveTrueAndTraineeList_User_UsernameIsNot(user1.getUsername())).thenReturn(trainerList);

        List<TrainerForTraineeProfileDTO> result = trainerService.getNotAssignedTrainers(user1.getUsername());

        assertNotNull(result);
        verify(trainerRepository, times(1)).findTrainersByUser_IsActiveTrueAndTraineeList_User_UsernameIsNot(user1.getUsername());
    }

    @Test
    void getTrainerListByUsernames_ShouldReturnListOfTrainers() throws TrainerNotFoundException {
        TrainerNameDTO trainerNameDTO = new TrainerNameDTO();
        trainerNameDTO.setUsername(user.getUsername());
        List<TrainerNameDTO> dtoList = List.of(trainerNameDTO);

        when(trainerRepository.getTrainerByUser_Username(anyString()))
                .thenReturn(Optional.of(trainer));

        List<Trainer> result = trainerService.getTrainerListByUsernames(dtoList);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(trainerRepository, times(1)).getTrainerByUser_Username(user.getUsername());
    }

    @Test
    void getTrainerByUsername_ShouldReturnTrainer() throws TrainerNotFoundException {
        when(trainerRepository.getTrainerByUser_Username(anyString()))
                .thenReturn(Optional.of(trainer));

        Trainer result = trainerService.getTrainerByUsername(user.getUsername());

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUser().getUsername());
        verify(trainerRepository, times(1)).getTrainerByUser_Username(user.getUsername());
    }

    @Test
    void getTrainerByUsername_ShouldThrowTrainerNotFoundException() {
        when(trainerRepository.getTrainerByUser_Username(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.getTrainerByUsername("unknown.username"));
        verify(trainerRepository, times(1)).getTrainerByUser_Username("unknown.username");
    }

    @Test
    void convertToDTOList_ShouldReturnDTOList() {
        List<Trainer> trainerList = List.of(trainer);
        when(traineeProfileConverter.toDtoList(anyList())).thenReturn(List.of(new TrainerForTraineeProfileDTO()));

        List<TrainerForTraineeProfileDTO> result = trainerService.convertToDTOList(trainerList);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(traineeProfileConverter, times(1)).toDtoList(trainerList);
    }



}
