package org.tolking.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.tolking.dto.LoginDTO;
import org.tolking.dto.criteria.CriteriaTrainerDTO;
import org.tolking.dto.trainer.TrainerCreateDTO;
import org.tolking.dto.trainer.TrainerForTraineeProfileDTO;
import org.tolking.dto.trainer.TrainerNameDTO;
import org.tolking.dto.trainer.TrainerProfileDTO;
import org.tolking.dto.trainer.TrainerUpdateDTO;
import org.tolking.dto.training.TrainingTrainerReadDTO;
import org.tolking.entity.Trainee;
import org.tolking.entity.Trainer;
import org.tolking.exception.TrainerNotFoundException;

import java.util.List;

@Validated
public interface TrainerService {

    /**
     * Creates a new Trainer based on the provided TrainerCreateDTO.
     *
     * @param dto the data transfer object containing the necessary information
     *            to create a new Trainer.
     * @return LoginDTO containing the username and password of the created Trainer.
     */
    @Validated
    LoginDTO create(@Valid @NotNull TrainerCreateDTO dto);

    /**
     * Retrieves the profile of a Trainer based on the provided username.
     *
     * @param username the username of the Trainer.
     * @return TrainerProfileDTO representing the Trainer's profile.
     * @throws TrainerNotFoundException if the Trainer is not found based on the provided username.
     */
    @Validated
    TrainerProfileDTO getProfile(@NotEmpty String username) throws TrainerNotFoundException;

    /**
     * Updates the Trainer's information based on the provided username and TrainerUpdateDTO.
     *
     * @param username         the username of the Trainer.
     * @param trainerUpdateDTO the data transfer object containing the updated information
     *                         for the Trainer.
     * @return TrainerProfileDTO representing the updated Trainer's profile.
     * @throws TrainerNotFoundException if the Trainer is not found based on the provided username.
     */
    @Validated
    TrainerProfileDTO update(@NotEmpty String username, @NotNull @Valid TrainerUpdateDTO trainerUpdateDTO) throws TrainerNotFoundException;

    /**
     * Updates the password for a Trainer based on the provided username and new password.
     *
     * @param username the username of the Trainer.
     * @param password the new password for the Trainer.
     * @throws TrainerNotFoundException if the Trainer is not found based on the provided username.
     */
    @Validated
    void updatePassword(@NotEmpty String username, @NotEmpty String password) throws TrainerNotFoundException;

    /**
     * Toggles the active status of a Trainer based on the provided username.
     *
     * @param username the username of the Trainer.
     * @throws TrainerNotFoundException if the Trainer is not found based on the provided username.
     */
    @Validated
    void toggleStatus(@NotEmpty String username) throws TrainerNotFoundException;

    /**
     * Retrieves a list of trainings for a Trainer based on the provided username and criteria.
     *
     * @param username the username of the Trainer.
     * @param criteria the criteria for filtering the list of trainings.
     * @return List of TrainingTrainerReadDTO representing the trainings that match the criteria.
     * @throws TrainerNotFoundException if the Trainer is not found based on the provided username.
     */
    @Validated
    List<TrainingTrainerReadDTO> getTrainingList(@NotEmpty String username, @NotNull CriteriaTrainerDTO criteria) throws TrainerNotFoundException;

    /**
     * Retrieves a list of trainers that are not assigned to the specified Trainee.
     *
     * @param traineeUsername the username of the Trainee.
     * @return List of TrainerForTraineeProfileDTO representing the profiles of trainers
     * that are not assigned to the Trainee.
     * @throws TrainerNotFoundException if the Trainee is not found based on the provided username.
     */
    @Validated
    List<TrainerForTraineeProfileDTO> getNotAssignedTrainers(@NotEmpty String traineeUsername) throws TrainerNotFoundException;

    /**
     * Retrieves a list of Trainers based on the provided list of TrainerNameDTOs.
     *
     * @param dtoList a list of TrainerNameDTO containing usernames of the Trainers to be retrieved.
     * @return List of Trainer representing the Trainers that match the provided usernames.
     * @throws TrainerNotFoundException if any of the Trainers are not found based on the provided usernames.
     */
    @Validated
    List<Trainer> getTrainerListByUsernames(@NotNull List<TrainerNameDTO> dtoList) throws TrainerNotFoundException;

    /**
     * Retrieves a Trainer based on the provided username.
     *
     * @param username the username of the Trainer to be retrieved.
     * @return Trainer representing the Trainer with the specified username.
     * @throws TrainerNotFoundException if the Trainer is not found based on the provided username.
     */
    @Validated
    Trainer getTrainerByUsername(@NotEmpty String username) throws TrainerNotFoundException;

    /**
     * Converts a list of Trainer entities to a list of TrainerForTraineeProfileDTOs.
     *
     * @param trainerList a list of Trainer entities to be converted.
     * @return List of TrainerForTraineeProfileDTO representing the converted Trainer entities.
     */
    @Validated
    List<TrainerForTraineeProfileDTO> convertToDTOList(@NotNull List<Trainer> trainerList);

    /**
     * Removes the association of a Trainee from all Trainers.
     *
     * @param trainee the Trainee whose associations are to be removed.
     */
    @Validated
    void removeTraineeAssociation(@NotNull Trainee trainee);
}
