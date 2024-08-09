package org.tolking;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.tolking.dao.TrainerDAO;
import org.tolking.dto.CriteriaTrainerDTO;
import org.tolking.dto.LoginDTO;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.converter.trainer.TrainerProfileConverter;
import org.tolking.dto.converter.trainer.TrainerUpdateConverter;
import org.tolking.dto.converter.training.TrainingReadConverter;
import org.tolking.dto.trainer.TrainerDTO;
import org.tolking.dto.trainer.TrainerProfileDTO;
import org.tolking.dto.trainer.TrainerUpdateDTO;
import org.tolking.dto.training.TrainingReadDTO;
import org.tolking.dto.user.UserCreateDTO;
import org.tolking.dto.user.UserProfileDTO;
import org.tolking.entity.Trainer;
import org.tolking.entity.Training;
import org.tolking.entity.TrainingType;
import org.tolking.entity.User;
import org.tolking.exception.UserNotFoundException;
import org.tolking.service.impl.TrainerServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class TrainerServiceTest {
    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private DTOConverter<Trainer, TrainerDTO> createConverter;

    @Mock
    private TrainerProfileConverter profileConverter;

    @Mock
    private TrainerUpdateConverter updateConverter;

    @Mock
    private TrainingReadConverter trainingConverter;


    @InjectMocks
    private TrainerServiceImpl trainerService;



    @Before
    public void setUp() {
        openMocks(this);
    }

    private static LoginDTO getLoginDTO() {
        return new LoginDTO("testuser", "password");
    }

    private static Trainer getTrainer() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setIsActive(true);
        user.setFirstName("test");
        user.setLastName("user");

        Trainer trainer = new Trainer();
        trainer.setTrainingType(new TrainingType("CARDIO"));
        trainer.setUser(user);
        return trainer;
    }


    @Test
    public void create_ShouldCreateTrainer() {
        TrainerDTO dto = new TrainerDTO();
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        dto.setUser(userCreateDTO);

        Trainer trainer = getTrainer();



        when(createConverter.toEntity(any(TrainerDTO.class))).thenReturn(trainer);
        when(trainerDAO.getUsername(anyString())).thenReturn("uniqueUsername");

        trainerService.create(dto);

        verify(trainerDAO, times(1)).create(trainer);
    }



    @Test
    public void getProfile_ShouldReturnTrainerProfileDTO() throws UserNotFoundException {
        TrainerProfileDTO trainerProfileDTO = new TrainerProfileDTO();
        trainerProfileDTO.setUser(new UserProfileDTO());
        Trainer trainer = getTrainer();
        LoginDTO loginDTO = getLoginDTO();

        when(trainerDAO.getByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.of(trainer));
        when(profileConverter.toDto(trainer)).thenReturn(trainerProfileDTO);

        trainerService.getProfile(loginDTO);

        verify(trainerDAO, times(1)).getByUsernameAndPassword(anyString(), anyString());
    }

    @Test
    public void updatePassword_ShouldUpdateTrainerPassword() throws UserNotFoundException {
        Trainer trainer = getTrainer();
        LoginDTO loginDTO = getLoginDTO();

        when(trainerDAO.getByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.of(trainer));

        trainerService.updatePassword(loginDTO, "newPassword");

        verify(trainerDAO, times(1)).update(trainer);
        assertEquals("newPassword", trainer.getUser().getPassword());
    }

    @Test
    public void update_ShouldUpdateTrainer() throws UserNotFoundException {
        TrainerUpdateDTO trainerUpdateDTO = new TrainerUpdateDTO();
        Trainer trainer = getTrainer();
        LoginDTO loginDTO = getLoginDTO();

        when(trainerDAO.getByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.of(trainer));
        when(updateConverter.updateEntity(trainer, trainerUpdateDTO)).thenReturn(trainer);

        trainerService.update(loginDTO, trainerUpdateDTO);

        verify(trainerDAO, times(1)).update(trainer);
    }

    @Test
    public void toggleStatus_ShouldToggleTrainerStatus() throws UserNotFoundException {
        LoginDTO loginDTO = getLoginDTO();
        Trainer trainer = getTrainer();

        when(trainerDAO.getByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.of(trainer));

        trainerService.toggleStatus(loginDTO);

        verify(trainerDAO, times(1)).toggleStatus(trainer);
    }

    @Test
    public void getTrainingList_ShouldReturnListOfTrainingReadDTO() {
        LoginDTO loginDTO = getLoginDTO();
        Trainer trainer = getTrainer();

        List<Training> trainingList = List.of(new Training());

        List<TrainingReadDTO> trainingReadList = List.of(new TrainingReadDTO());
        when(trainerDAO.getByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.of(trainer));
        when(trainerDAO.getTrainingsByCriteria(any(), any(), anyString(), anyString())).thenReturn(trainingList);
        when(trainingConverter.toDtoList(anyList())).thenReturn(trainingReadList);

        CriteriaTrainerDTO criteria = new CriteriaTrainerDTO();
        criteria.setFrom(new Date());
        criteria.setTo(new Date());
        criteria.setTraineeUsername("trainee");

        List<TrainingReadDTO> result = trainerService.getTrainingList(loginDTO, criteria);

        assertNotNull(result);
        verify(trainerDAO, times(1)).getTrainingsByCriteria(any(), any(), anyString(), anyString());
    }

    @Test
    public void getTrainer_ShouldThrowExceptionIfNotFound() {
        LoginDTO loginDTO = getLoginDTO();
        when(trainerDAO.getByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> trainerService.getProfile(loginDTO));
        verify(trainerDAO, times(1)).getByUsernameAndPassword(anyString(), anyString());
    }
}
