package org.tolking.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.tolking.dto.LoginDTO;
import org.tolking.dto.criteria.CriteriaTraineeDTO;
import org.tolking.dto.trainee.TraineeCreateDTO;
import org.tolking.dto.trainee.TraineeProfileDTO;
import org.tolking.dto.trainee.TraineeUpdateDTO;
import org.tolking.dto.trainer.TrainerForTraineeProfileDTO;
import org.tolking.dto.trainer.TrainerNameDTO;
import org.tolking.dto.training.TrainingTraineeReadDTO;
import org.tolking.entity.Trainee;
import org.tolking.exception.TraineeNotFoundException;
import org.tolking.exception.TrainerNotFoundException;

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
    LoginDTO create(@Valid @NotNull TraineeCreateDTO dto);

    /**
     * Retrieves the profile of a Trainee based on the username.
     *
     * @param username the username of the Trainee.
     * @return TraineeProfileDTO representing the Trainee's profile.
     * @throws TraineeNotFoundException if the Trainee is not found based on the provided username.
     */
    TraineeProfileDTO getProfile(@NotEmpty String username) throws TraineeNotFoundException;

    /**
     * Updates the password for a Trainee based on the username and new password.
     *
     * @param username   the username of the Trainee.
     * @param newPassword the new password for the Trainee.
     * @throws TraineeNotFoundException if the Trainee is not found based on the provided username.
     */
    void updatePassword(@NotEmpty String username, @NotEmpty String newPassword) throws TraineeNotFoundException;

    /**
     * Updates the Trainee's information based on the username and the provided
     * TraineeUpdateDTO.
     *
     * @param username the username of the Trainee.
     * @param traineeUpdateDTO the data transfer object containing the updated information
     *                         for the Trainee.
     * @return TraineeProfileDTO representing the updated Trainee's profile.
     * @throws TraineeNotFoundException if the Trainee is not found based on the provided username.
     */
    TraineeProfileDTO update(@NotEmpty String username, @Valid @NotNull TraineeUpdateDTO traineeUpdateDTO) throws TraineeNotFoundException;

    /**
     * Toggles the active status of a Trainee based on the username.
     *
     * @param username the username of the Trainee.
     * @throws TraineeNotFoundException if the Trainee is not found based on the provided username.
     */
    void toggleStatus(@NotEmpty String username) throws TraineeNotFoundException;

    /**
     * Deletes a Trainee based on the username.
     *
     * @param username the username of the Trainee.
     * @throws TraineeNotFoundException if the Trainee is not found based on the provided username.
     */
    void delete(@NotEmpty String username) throws TraineeNotFoundException;

    /**
     * Retrieves a list of trainings for a Trainee based on the username and
     * the provided criteria.
     *
     * @param username the username of the Trainee.
     * @param criteria the criteria for filtering the list of trainings.
     * @return List<TrainingTraineeReadDTO> a list of data transfer objects representing
     *         the trainings that match the criteria.
     * @throws TraineeNotFoundException if the Trainee is not found based on the provided username.
     */
    List<TrainingTraineeReadDTO> getTrainingList(@NotEmpty String username, @NotNull CriteriaTraineeDTO criteria) throws TraineeNotFoundException;

    /**
     * Retrieves a list of trainers that are not assigned to the Trainee.
     *
     * @param username the username of the Trainee.
     * @return List<TrainerForTraineeProfileDTO> a list of data transfer objects representing
     *         the profiles of trainers that are not assigned to the Trainee.
     * @throws TraineeNotFoundException if the Trainee is not found based on the provided username.
     */
    List<TrainerForTraineeProfileDTO> getNotAssignedTrainers(@NotEmpty String username) throws TraineeNotFoundException;

    /**
     * Updates the list of assigned trainers for a Trainee based on the username.
     *
     * @param username            the username of the Trainee.
     * @param trainerNameDTOList the list of TrainerNameDTO containing the usernames of trainers
     *                           to be assigned to the Trainee.
     * @return List<TrainerForTraineeProfileDTO> a list of data transfer objects representing
     *         the profiles of trainers assigned to the Trainee after the update.
     * @throws TrainerNotFoundException if any of the specified trainers are not found.
     * @throws TraineeNotFoundException if the Trainee is not found based on the provided username.
     * @throws IllegalArgumentException if the trainer list is empty.
     */
    List<TrainerForTraineeProfileDTO> updateTrainerList(@NotEmpty String username, @Valid @NotNull List<TrainerNameDTO> trainerNameDTOList)
            throws TrainerNotFoundException, TraineeNotFoundException, IllegalArgumentException;

    /**
     * Retrieves a Trainee entity based on the provided username.
     *
     * @param username the username of the Trainee.
     * @return Trainee the Trainee entity corresponding to the provided username.
     * @throws TraineeNotFoundException if no Trainee is found with the provided username.
     */
    Trainee getTraineeByUsername(@NotEmpty String username) throws TraineeNotFoundException;
}
