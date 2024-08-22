package org.tolking.service;

import org.tolking.dto.criteria.CriteriaTraineeDTO;
import org.tolking.dto.criteria.CriteriaTrainerDTO;
import org.tolking.dto.training.TrainingDTO;
import org.tolking.dto.training.TrainingTraineeReadDTO;
import org.tolking.dto.training.TrainingTrainerReadDTO;
import org.tolking.entity.Trainee;
import org.tolking.exception.TraineeNotFoundException;
import org.tolking.exception.TrainerNotFoundException;

import java.util.List;

public interface TrainingService {

    /**
     * Retrieves a list of trainings for a Trainee based on the provided criteria.
     *
     * @param username the username of the Trainee.
     * @param criteria the criteria for filtering the list of trainings.
     * @return List of TrainingTraineeReadDTO representing the trainings that match the criteria.
     */
    List<TrainingTraineeReadDTO> getTraineeTrainingListByCriteria(String username, CriteriaTraineeDTO criteria);

    /**
     * Retrieves a list of trainings for a Trainer based on the provided criteria.
     *
     * @param username the username of the Trainer.
     * @param criteria the criteria for filtering the list of trainings.
     * @return List of TrainingTrainerReadDTO representing the trainings that match the criteria.
     */
    List<TrainingTrainerReadDTO> getTrainerTrainingListByCriteria(String username, CriteriaTrainerDTO criteria);

    /**
     * Creates a new training session for a Trainee.
     *
     * @param trainee the Trainee for whom the training session is to be created.
     * @param dto the data transfer object containing the details of the training session to be created.
     * @throws TrainerNotFoundException if the Trainer specified in the DTO is not found.
     * @throws TraineeNotFoundException if the Trainee is not found.
     */
    void createTraining(Trainee trainee, TrainingDTO dto) throws TrainerNotFoundException, TraineeNotFoundException;
}
