package org.tolking.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tolking.entity.TrainingType;
import org.tolking.enums.TrainingsType;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingTypeRepository extends CrudRepository<TrainingType, Integer> {
    Optional<TrainingType> getTrainingTypeByName(TrainingsType name);

    List<TrainingType> getAllBy();
}
