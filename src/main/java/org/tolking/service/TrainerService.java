package org.tolking.service;

import org.tolking.dto.LoginDTO;
import org.tolking.dto.LoginNewPassword;
import org.tolking.dto.criteria.CriteriaTrainerDTO;
import org.tolking.dto.trainer.*;
import org.tolking.dto.training.TrainingTrainerReadDTO;
import org.tolking.entity.Trainee;
import org.tolking.entity.Trainer;
import org.tolking.exception.TrainerNotFoundException;
import org.tolking.exception.UserNotFoundException;

import java.util.List;

public interface TrainerService {

    /**
     * Creates a new Trainer based on the provided TrainerCreateDTO.
     *
     * @param dto the data transfer object containing the necessary information
     *            to create a new Trainer.
     * @return LoginDTO containing the username and password of the created Trainer.
     */
    LoginDTO create(TrainerCreateDTO dto);

    /**
     * Retrieves the profile of a Trainer based on login credentials.
     *
     * @param dto the data transfer object containing the username and password
     *            for authenticating the Trainer.
     * @return TrainerProfileDTO representing the Trainer's profile.
     * @throws UserNotFoundException if the Trainer is not found based on the provided credentials.
     */
    TrainerProfileDTO getProfile(LoginDTO dto) throws UserNotFoundException;

    /**
     * Updates the Trainer's information based on login credentials and the provided
     * TrainerUpdateDTO.
     *
     * @param loginDTO the data transfer object containing the username and password
     *                 for authenticating the Trainer.
     * @param trainerUpdateDTO the data transfer object containing the updated information
     *                         for the Trainer.
     * @return TrainerProfileDTO representing the updated Trainer's profile.
     * @throws UserNotFoundException if the Trainer is not found based on the provided credentials.
     */
    TrainerProfileDTO update(LoginDTO loginDTO, TrainerUpdateDTO trainerUpdateDTO) throws UserNotFoundException;

    /**
     * Updates the password for a Trainer based on login credentials.
     *
     * @param dto the data transfer object containing the username, current password, and new password
     *            for authenticating the Trainer.
     * @throws UserNotFoundException if the Trainer is not found based on the provided credentials.
     */
    void updatePassword(LoginNewPassword dto) throws UserNotFoundException;

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
     * @return List of TrainingTrainerReadDTO representing the trainings that match the criteria.
     * @throws UserNotFoundException if the Trainer is not found based on the provided credentials.
     */
    List<TrainingTrainerReadDTO> getTrainingList(LoginDTO loginDTO, CriteriaTrainerDTO criteria) throws UserNotFoundException;

    /**
     * Retrieves a list of trainers that are not assigned to the specified Trainee.
     *
     * @param traineeUsername the username of the Trainee.
     * @return List of TrainerForTraineeProfileDTO representing the profiles of trainers
     *         that are not assigned to the Trainee.
     * @throws UserNotFoundException if the Trainee is not found based on the provided username.
     */
    List<TrainerForTraineeProfileDTO> getNotAssignedTrainers(String traineeUsername) throws UserNotFoundException;

    /**
     * Retrieves a list of Trainers based on the provided list of TrainerNameDTOs.
     *
     * @param dtoList a list of TrainerNameDTO containing usernames of the Trainers to be retrieved.
     * @return List of Trainer representing the Trainers that match the provided usernames.
     * @throws TrainerNotFoundException if any of the Trainers are not found based on the provided usernames.
     */
    List<Trainer> getTrainerListByUsernames(List<TrainerNameDTO> dtoList) throws TrainerNotFoundException;

    /**
     * Retrieves a Trainer based on the provided username.
     *
     * @param username the username of the Trainer to be retrieved.
     * @return Trainer representing the Trainer with the specified username.
     * @throws TrainerNotFoundException if the Trainer is not found based on the provided username.
     */
    Trainer getTrainerByUsername(String username) throws TrainerNotFoundException;

    /**
     * Converts a list of Trainer entities to a list of TrainerForTraineeProfileDTOs.
     *
     * @param trainerList a list of Trainer entities to be converted.
     * @return List of TrainerForTraineeProfileDTO representing the converted Trainer entities.
     */
    List<TrainerForTraineeProfileDTO> convertToDTOList(List<Trainer> trainerList);

    /**
     * Removes the association of a Trainee from all Trainers.
     *
     * @param trainee the Trainee whose associations are to be removed.
     */
    void removeTraineeAssociation(Trainee trainee);
}
