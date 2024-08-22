package org.tolking.service;

import org.tolking.dto.trainingType.TrainingTypeDTO;
import org.tolking.entity.TrainingType;
import org.tolking.enums.TrainingsType;
import org.tolking.exception.TrainingTypeNotFoundException;

import java.util.List;

public interface TrainingTypeService {
    /**
     * Creates a new TrainingType with the specified name.
     *
     * @param name the name of the training type to be created.
     */
    void create(String name);

    /**
     * Finds a TrainingType by its name.
     *
     * @param name the name of the training type to find.
     * @return TrainingType the TrainingType entity found by the specified name.
     * @throws TrainingTypeNotFoundException if no TrainingType is found with the specified name.
     */
    TrainingType findByName(TrainingsType name) throws TrainingTypeNotFoundException;

    /**
     * Gets a List of all TrainingType.
     * @return List of TrainingType
     */
    List<TrainingTypeDTO> getAll();

}
