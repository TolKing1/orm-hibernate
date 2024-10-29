package org.tolking.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.tolking.dto.criteria.CriteriaTraineeDTO;
import org.tolking.dto.criteria.CriteriaTrainerDTO;
import org.tolking.dto.training.TrainingDTO;
import org.tolking.dto.training.TrainingTraineeReadDTO;
import org.tolking.dto.training.TrainingTrainerReadDTO;
import org.tolking.entity.Trainee;
import org.tolking.exception.TraineeNotFoundException;
import org.tolking.exception.TrainerNotFoundException;
import org.tolking.exception.TrainingNotFoundException;
import org.tolking.external_dto.TrainingEventDTO;

import java.util.List;

@Validated
public interface TrainingService {

    /**
     * Retrieves a list of trainings for a Trainee based on the provided criteria.
     *
     * @param username the username of the Trainee.
     * @param criteria the criteria for filtering the list of trainings.
     * @return List of TrainingTraineeReadDTO representing the trainings that match the criteria.
     */
    @Validated
    List<TrainingTraineeReadDTO> getTraineeTrainingListByCriteria(@NotEmpty String username, @NotNull CriteriaTraineeDTO criteria);

    /**
     * Retrieves a list of trainings for a Trainer based on the provided criteria.
     *
     * @param username the username of the Trainer.
     * @param criteria the criteria for filtering the list of trainings.
     * @return List of TrainingTrainerReadDTO representing the trainings that match the criteria.
     */
    @Validated
    List<TrainingTrainerReadDTO> getTrainerTrainingListByCriteria(@NotEmpty String username, @NotNull CriteriaTrainerDTO criteria);

    /**
     * Creates a new training session for a Trainee.
     *
     * @param trainee the Trainee for whom the training session is to be created.
     * @param dto     the data transfer object containing the details of the training session to be created.
     * @return Created training
     * @throws TrainerNotFoundException if the Trainer specified in the DTO is not found.
     * @throws TraineeNotFoundException if the Trainee is not found.
     */
    @Validated
    TrainingEventDTO createTraining(@NotNull Trainee trainee, @NotNull @Valid TrainingDTO dto) throws TrainerNotFoundException, TraineeNotFoundException;

    /**
     * Cancels an existing training session by its ID.
     *
     * @param id the ID of the training session to be canceled.
     * @throws TrainingNotFoundException if the training session with the specified ID is not found.
     */
    TrainingEventDTO cancelTraining(String traineeUsername, long id) throws TrainingNotFoundException;
}
