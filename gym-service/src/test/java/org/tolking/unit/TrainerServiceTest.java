package org.tolking.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import org.tolking.enums.TrainingsType;
import org.tolking.exception.TrainerNotFoundException;
import org.tolking.repository.TrainerRepository;
import org.tolking.service.TrainingService;
import org.tolking.service.TrainingTypeService;
import org.tolking.service.UserService;
import org.tolking.service.impl.TrainerServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private PasswordEncoder passwordEncoder;

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
    private String traineeUsername;
    private String traineePassword;
    private String trainerUsername;
    private String trainerPassword;
    private User user;
    private User user1;

    @BeforeEach
    public void setUp() {
        trainerCreateDTO = new TrainerCreateDTO();
        trainerUpdateDTO = new TrainerUpdateDTO();
        trainingType = new TrainingType("CARDIO");

        traineeUsername = "Tolek12";
        traineePassword = "121231232";
        trainerUsername = "BBB";
        trainerPassword = "1312313123";

        trainer = new Trainer();
        trainer.setTrainingType(trainingType);
        user = new User();
        user.setUsername(trainerUsername);
        user.setPassword(trainerPassword);
        user.setFirstName("Tolek");
        user.setLastName("Bek");
        user.setIsActive(true);
        trainer.setUser(user);

        trainee = new Trainee();
        user1 = new User();
        user1.setUsername(traineeUsername);
        user1.setPassword(traineePassword);
        user1.setFirstName("Tolek");
        user1.setLastName("Bek");
        user1.setIsActive(true);
        trainee.setUser(user1);
        trainee.setTrainerList(List.of(trainer));

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
    public void getProfile_shouldReturnTrainerProfile_whenTrainerExists() {
        when(trainerRepository.getTrainerByUser_Username(trainerUsername))
                .thenReturn(Optional.of(trainer));
        when(profileConverter.toDto(trainer)).thenReturn(new TrainerProfileDTO());

        trainerService.getProfile(trainerUsername);

        verify(trainerRepository, times(1))
                .getTrainerByUser_Username(trainerUsername);
        verify(profileConverter, times(1)).toDto(trainer);
    }

    @Test
    public void getProfile_shouldThrowUserNotFoundException_whenTrainerDoesNotExist() {
        when(trainerRepository.getTrainerByUser_Username(trainerUsername))
                .thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.getProfile(trainerUsername));
        verify(trainerRepository, times(1))
                .getTrainerByUser_Username(trainerUsername);
    }

    @Test
    public void update_shouldUpdateTrainerProfile_whenTrainerExists() {
        Trainer updatedTrainer = new Trainer();

        when(trainerRepository.getTrainerByUser_Username(trainerUsername))
                .thenReturn(Optional.of(trainer));
        when(updateConverter.updateEntity(trainer, trainerUpdateDTO)).thenReturn(updatedTrainer);
        when(trainerRepository.save(any())).thenReturn(updatedTrainer);

        trainerService.update(trainerUsername, trainerUpdateDTO);

        verify(trainerRepository, times(1)).save(updatedTrainer);
    }

    @Test
    public void update_shouldThrowUserNotFoundException_whenTrainerDoesNotExist() {
        when(trainerRepository.getTrainerByUser_Username(trainerUsername))
                .thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.update(trainerUsername, trainerUpdateDTO));
        verify(trainerRepository, times(1))
                .getTrainerByUser_Username(trainerUsername);
    }

    @Test
    public void updatePassword_shouldUpdatePassword_whenTrainerExists() {
        String newPassword = "newPassword";
        String encoded = "asdljwoajdwoawjodlafgsdgsfdgfd";

        when(trainerRepository.getTrainerByUser_Username(traineeUsername))
                .thenReturn(Optional.of(trainer));

        when(passwordEncoder.encode(newPassword)).thenReturn(encoded);

        trainerService.updatePassword(traineeUsername, newPassword);

        assertEquals(encoded, trainer.getUser().getPassword());
        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    public void updatePassword_shouldThrowUserNotFoundException_whenTrainerDoesNotExist() {
        String loginNewPassword = "1213123";
        when(trainerRepository.getTrainerByUser_Username(trainerUsername))
                .thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.updatePassword(trainerUsername, loginNewPassword));
        verify(trainerRepository, times(1))
                .getTrainerByUser_Username(trainerUsername);
    }

    @Test
    public void toggleStatus_shouldToggleStatus_whenTrainerExists() {
        when(trainerRepository.getTrainerByUser_Username(trainerUsername))
                .thenReturn(Optional.of(trainer));

        trainerService.toggleStatus(trainerUsername);

        verify(trainerRepository, times(1)).save(trainer);
        verify(trainerRepository, times(1))
                .getTrainerByUser_Username(trainerUsername);
    }

    @Test
    public void toggleStatus_shouldThrowUserNotFoundException_whenTrainerDoesNotExist() {
        when(trainerRepository.getTrainerByUser_Username(trainerUsername))
                .thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.toggleStatus(trainerUsername));
        verify(trainerRepository, times(1))
                .getTrainerByUser_Username(trainerUsername);
    }

    @Test
    public void getTrainingList_shouldReturnTrainingList_whenTrainerExists() {
        CriteriaTrainerDTO criteria = new CriteriaTrainerDTO(); // Initialize fields as needed
        when(trainerRepository.getTrainerByUser_Username(trainerUsername))
                .thenReturn(Optional.of(trainer));
        when(trainingService.getTrainerTrainingListByCriteria(anyString(), eq(criteria)))
                .thenReturn(List.of(new TrainingTrainerReadDTO()));

        List<TrainingTrainerReadDTO> result = trainerService.getTrainingList(trainerUsername, criteria);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(trainingService, times(1)).getTrainerTrainingListByCriteria(anyString(), eq(criteria));
    }

    @Test
    public void getNotAssignedTrainers_shouldReturnTrainerList() {
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
        verify(traineeProfileConverter, times(1)).toDtoList(trainerList);
    }

    @Test
    void remove_associations() {
        trainerService.removeTraineeAssociation(trainee);

        verify(trainerRepository, times(1)).save(trainer);
    }
}
