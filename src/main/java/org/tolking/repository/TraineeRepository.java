package org.tolking.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tolking.entity.Trainee;

import java.util.Optional;

@Repository
public interface TraineeRepository extends CrudRepository<Trainee, Long> {
    Optional<Trainee> getTraineeByUser_Username(String username);
}
