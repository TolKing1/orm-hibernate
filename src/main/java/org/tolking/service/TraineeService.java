package org.tolking.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.tolking.dto.LoginDTO;
import org.tolking.dto.LoginNewPassword;
import org.tolking.dto.criteria.CriteriaTraineeDTO;
import org.tolking.dto.trainee.TraineeCreateDTO;
import org.tolking.dto.trainee.TraineeProfileDTO;
import org.tolking.dto.trainee.TraineeUpdateDTO;
import org.tolking.dto.trainer.TrainerForTraineeProfileDTO;
import org.tolking.dto.trainer.TrainerNameDTO;
import org.tolking.dto.training.TrainingTraineeReadDTO;
import org.tolking.entity.Trainee;
import org.tolking.exception.TrainerNotFoundException;
import org.tolking.exception.UserNotFoundException;

import java.util.List;

@Validated
public interface TraineeService {

    /**
     * Creates a new Trainee based on the provided TraineeCreateDTO.
     *
     * @param dto the data transfer object containing the necessary information
     *            to create a new Trainee.
     * @return LoginDTO containing the generated username and password for the Trainee.
     */
    @Validated
    LoginDTO create(@Valid @NotNull TraineeCreateDTO dto);

    /**
     * Retrieves the profile of a Trainee based on login credentials.
     *
     * @param dto the data transfer object containing the username and password
     *            for authenticating the Trainee.
     * @return TraineeProfileDTO the data transfer object representing the Trainee's profile.
     * @throws UserNotFoundException if the Trainee is not found based on the provided credentials.
     */
    @Validated
    TraineeProfileDTO getProfile(@Valid @NotNull LoginDTO dto) throws UserNotFoundException;

    /**
     * Updates the password for a Trainee based on login credentials.
     *
     * @param dto the data transfer object containing the username, password and new password
     *            for authenticating the Trainee.
     * @throws UserNotFoundException if the Trainee is not found based on the provided credentials.
     */
    @Validated
    void updatePassword(@Valid @NotNull LoginNewPassword dto) throws UserNotFoundException;

    /**
     * Updates the Trainee's information based on login credentials and the provided
     * TraineeUpdateDTO.
     *
     * @param loginDTO the data transfer object containing the username and password
     *                 for authenticating the Trainee.
     * @param traineeUpdateDTO the data transfer object containing the updated information
     *                         for the Trainee.
     * @return TraineeProfileDTO the data transfer object representing the Trainee's updated profile.
     * @throws UserNotFoundException if the Trainee is not found based on the provided credentials.
     */
    @Validated
    TraineeProfileDTO update(@Valid @NotNull LoginDTO loginDTO,@NotNull TraineeUpdateDTO traineeUpdateDTO) throws UserNotFoundException;

    /**
     * Toggles the active status of a Trainee based on login credentials.
     *
     * @param loginDTO the data transfer object containing the username and password
     *                 for authenticating the Trainee.
     * @throws UserNotFoundException if the Trainee is not found based on the provided credentials.
     */
    @Validated
    void toggleStatus(@Valid @NotNull LoginDTO loginDTO) throws UserNotFoundException;

    /**
     * Deletes a Trainee based on login credentials.
     *
     * @param dto the data transfer object containing the username and password
     *            for authenticating the Trainee.
     * @throws UserNotFoundException if the Trainee is not found based on the provided credentials.
     */
    @Validated
    void delete(@Valid @NotNull LoginDTO dto) throws UserNotFoundException;

    /**
     * Retrieves a list of trainings for a Trainee based on login credentials and
     * the provided criteria.
     *
     * @param loginDTO the data transfer object containing the username and password
     *                 for authenticating the Trainee.
     * @param criteria the criteria for filtering the list of trainings.
     * @return List<TrainingTraineeReadDTO> a list of data transfer objects representing
     *         the trainings that match the criteria.
     * @throws UserNotFoundException if the Trainee is not found based on the provided credentials.
     */
    @Validated
    List<TrainingTraineeReadDTO> getTrainingList(@Valid @NotNull LoginDTO loginDTO, @NotNull CriteriaTraineeDTO criteria) throws UserNotFoundException;

    /**
     * Retrieves a list of trainers that are not assigned to the Trainee.
     *
     * @param loginDTO the data transfer object containing the username and password
     *                 for authenticating the Trainee.
     * @return List<TrainerForTraineeProfileDTO> a list of data transfer objects representing
     *         the profiles of trainers that are not assigned to the Trainee.
     * @throws UserNotFoundException if the Trainee is not found based on the provided credentials.
     */
    @Validated
    List<TrainerForTraineeProfileDTO> getNotAssignedTrainers(@Valid @NotNull LoginDTO loginDTO) throws UserNotFoundException;

    /**
     * Updates the list of assigned trainers for a Trainee based on login credentials.
     *
     * @param loginDTO the data transfer object containing the username and password
     *                 for authenticating the Trainee.
     * @param trainerNameDTOList the list of TrainerNameDTO containing the usernames of trainers
     *                           to be assigned to the Trainee.
     * @return List<TrainerForTraineeProfileDTO> a list of data transfer objects representing
     *         the profiles of trainers assigned to the Trainee after update.
     * @throws TrainerNotFoundException if any of the specified trainers are not found.
     * @throws UserNotFoundException if the Trainee is not found based on the provided credentials.
     * @throws IllegalArgumentException if the Trainer list is empty.
     */
    @Validated
    List<TrainerForTraineeProfileDTO> updateTrainerList(@Valid @NotNull LoginDTO loginDTO,@Valid @NotNull List<TrainerNameDTO> trainerNameDTOList) throws TrainerNotFoundException, UserNotFoundException, IllegalArgumentException;

    /**
     * Retrieves a Trainee entity based on the provided login credentials.
     *
     * @param dto the data transfer object containing the username and password
     *            for authenticating the Trainee.
     * @return Trainee the Trainee entity corresponding to the provided credentials.
     * @throws UserNotFoundException if no Trainee is found with the provided credentials.
     */
    @Validated
    Trainee getTraineeByLogin(@Valid @NotNull LoginDTO dto) throws UserNotFoundException;
}
