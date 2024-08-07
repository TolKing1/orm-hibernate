package org.tolking.service;

import org.tolking.entity.TrainingType;
import org.tolking.exception.TrainingTypeNotFoundException;

public interface TrainingTypeService {
    void create(String name);

    TrainingType findByName(String name) throws TrainingTypeNotFoundException;
}
