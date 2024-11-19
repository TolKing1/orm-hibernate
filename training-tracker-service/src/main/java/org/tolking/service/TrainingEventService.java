package org.tolking.service;

import org.tolking.dto.TrainerSummaryDTO;
import org.tolking.external_dto.TrainingEventDTO;

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
     * @return a list of {@link TrainerSummaryDTO} objects representing the summary of all trainers.
     */
    List<TrainerSummaryDTO> getAllSummary();
}
