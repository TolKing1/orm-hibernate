package org.tolking.repository;

import org.springframework.data.repository.CrudRepository;
import org.tolking.entity.Trainee;

import java.util.Optional;

public interface TraineeRepository extends CrudRepository<Trainee, Long> {
    Optional<Trainee> getTraineeByUser_Username(String username);
}
