package org.tolking.service;

import org.tolking.dto.CriteriaTrainerDTO;
import org.tolking.dto.LoginDTO;
import org.tolking.dto.trainer.TrainerDTO;
import org.tolking.dto.trainer.TrainerProfileDTO;
import org.tolking.dto.trainer.TrainerUpdateDTO;
import org.tolking.dto.training.TrainingReadDTO;
import org.tolking.exception.UserNotFoundException;

import java.util.List;

public interface TrainerService {
    /**
     * Creates a new Trainer based on the provided TrainerDTO.
     *
     * @param dto the data transfer object containing the necessary information
     *            to create a new Trainer.
     */
    void create(TrainerDTO dto);

    /**
     * Retrieves the profile of a Trainer based on login credentials.
     *
     * @param dto the data transfer object containing the username and password
     *            for authenticating the Trainer.
     * @return TrainerProfileDTO the data transfer object representing the Trainer's profile.
     * @throws UserNotFoundException if the Trainer is not found based on the provided credentials.
     */
    TrainerProfileDTO getProfile(LoginDTO dto) throws UserNotFoundException;

    /**
     * Updates the password for a Trainer based on login credentials.
     *
     * @param dto the data transfer object containing the username and password
     *            for authenticating the Trainer.
     * @param newPassword the new password to set for the Trainer.
     * @throws UserNotFoundException if the Trainer is not found based on the provided credentials.
     */
    void updatePassword(LoginDTO dto, String newPassword) throws UserNotFoundException;

    /**
     * Updates the Trainer's information based on login credentials and the provided
     * TrainerUpdateDTO.
     *
     * @param loginDTO the data transfer object containing the username and password
     *                 for authenticating the Trainer.
     * @param trainerUpdateDTO the data transfer object containing the updated information
     *                         for the Trainer.
     * @throws UserNotFoundException if the Trainer is not found based on the provided credentials.
     */
    void update(LoginDTO loginDTO, TrainerUpdateDTO trainerUpdateDTO) throws UserNotFoundException;

    /**
     * Toggles the active status of a Trainer based on login credentials.
     *
     * @param loginDTO the data transfer object containing the username and password
     *                 for authenticating the Trainer.
     * @throws UserNotFoundException if the Trainer is not found based on the provided credentials.
     */
    void toggleStatus(LoginDTO loginDTO) throws UserNotFoundException;

    /**
     * Retrieves a list of trainings for a Trainer based on login credentials and
     * the provided criteria.
     *
     * @param loginDTO the data transfer object containing the username and password
     *                 for authenticating the Trainer.
     * @param criteria the criteria for filtering the list of trainings.
     * @throws UserNotFoundException if the Trainer is not found based on the provided credentials.
     * @return List<TrainingReadDTO> a list of data transfer objects representing
     *         the trainings that match the criteria.
     */
    List<TrainingReadDTO> getTrainingList(LoginDTO loginDTO, CriteriaTrainerDTO criteria) throws UserNotFoundException;
}
