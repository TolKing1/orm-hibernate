package org.tolking.service;

import org.tolking.dto.CriteriaTraineeDTO;
import org.tolking.dto.LoginDTO;
import org.tolking.dto.trainee.TraineeDTO;
import org.tolking.dto.trainee.TraineeProfileDTO;
import org.tolking.dto.trainer.TrainerProfileDTO;
import org.tolking.dto.training.TrainingDTO;
import org.tolking.dto.training.TrainingReadDTO;
import org.tolking.exception.UserNotFoundException;

import java.util.List;

public interface TraineeService {
    /**
     * Creates a new Trainee based on the provided TraineeDTO.
     *
     * @param dto the data transfer object containing the necessary information
     *            to create a new Trainee.
     */
    void create(TraineeDTO dto);

    /**
     * Retrieves the profile of a Trainee based on login credentials.
     *
     * @param dto the data transfer object containing the username and password
     *            for authenticating the Trainee.
     * @return TraineeProfileDTO the data transfer object representing the Trainee's profile.
     * @throws UserNotFoundException if the Trainee is not found based on the provided credentials.
     */
    TraineeProfileDTO getProfile(LoginDTO dto) throws UserNotFoundException;

    /**
     * Updates the password for a Trainee based on login credentials.
     *
     * @param dto the data transfer object containing the username and password
     *            for authenticating the Trainee.
     * @param newPassword the new password to set for the Trainee.
     * @throws UserNotFoundException if the Trainee is not found based on the provided credentials.
     */
    void updatePassword(LoginDTO dto, String newPassword) throws UserNotFoundException;

    /**
     * Updates the Trainee's information based on login credentials and the provided
     * TraineeProfileDTO.
     *
     * @param loginDTO the data transfer object containing the username and password
     *                 for authenticating the Trainee.
     * @param traineeUpdateDTO the data transfer object containing the updated information
     *                         for the Trainee.
     * @throws UserNotFoundException if the Trainee is not found based on the provided credentials.
     */
    void update(LoginDTO loginDTO, TraineeProfileDTO traineeUpdateDTO) throws UserNotFoundException;

    /**
     * Toggles the active status of a Trainee based on login credentials.
     *
     * @param loginDTO the data transfer object containing the username and password
     *                 for authenticating the Trainee.
     * @throws UserNotFoundException if the Trainee is not found based on the provided credentials.
     */
    void toggleStatus(LoginDTO loginDTO) throws UserNotFoundException;

    /**
     * Deletes a Trainee based on login credentials.
     *
     * @param dto the data transfer object containing the username and password
     *            for authenticating the Trainee.
     * @throws UserNotFoundException if the Trainee is not found based on the provided credentials.
     */
    void delete(LoginDTO dto) throws UserNotFoundException;

    /**
     * Adds a new training to the Trainee's list of trainings.
     *
     * @param loginDTO the data transfer object containing the username and password
     *                 for authenticating the Trainee.
     * @param dto the data transfer object containing the details of the training
     *            to be added.
     * @throws UserNotFoundException if the Trainee is not found based on the provided credentials.
     */
    void addTraining(LoginDTO loginDTO, TrainingDTO dto) throws UserNotFoundException;

    /**
     * Retrieves a list of trainings for a Trainee based on login credentials and
     * the provided criteria.
     *
     * @param loginDTO the data transfer object containing the username and password
     *                 for authenticating the Trainee.
     * @param criteria the criteria for filtering the list of trainings.
     * @return List<TrainingReadDTO> a list of data transfer objects representing
     *         the trainings that match the criteria.
     * @throws UserNotFoundException if the Trainee is not found based on the provided credentials.
     */
    List<TrainingReadDTO> getTrainingList(LoginDTO loginDTO, CriteriaTraineeDTO criteria) throws UserNotFoundException;

    /**
     * Retrieves a list of trainers that are not assigned to the Trainee.
     *
     * @param loginDTO the data transfer object containing the username and password
     *                 for authenticating the Trainee.
     * @return List<TrainerProfileDTO> a list of data transfer objects representing
     *         the profiles of trainers that are not assigned to the Trainee.
     * @throws UserNotFoundException if the Trainee is not found based on the provided credentials.
     */
    List<TrainerProfileDTO> getNotAssignedTrainers(LoginDTO loginDTO) throws UserNotFoundException;
}
