package org.tolking.training_event_service.service;

import org.tolking.training_event_service.dto.TrainingEventDTO;
import org.tolking.training_event_service.model.TrainerSummary;

import java.util.List;

public interface TrainingEventService {

    /**
     * Creates a new training event.
     *
     * @param trainingEventDTO the data transfer object containing the details of the training event to be created.
     */
    void create(TrainingEventDTO trainingEventDTO);

    /**
     * Retrieves a summary of all trainers.
     *
     * @return a list of {@link TrainerSummary} objects representing the summary of all trainers.
     */
    List<TrainerSummary> getAllSummary();
}
