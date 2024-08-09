package org.tolking.service;

import org.tolking.entity.TrainingType;
import org.tolking.exception.TrainingTypeNotFoundException;

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
    TrainingType findByName(String name) throws TrainingTypeNotFoundException;
}
