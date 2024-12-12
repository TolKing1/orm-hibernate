package org.tolking.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tolking.entity.TrainerSummary;

import java.util.List;
import java.util.Optional;

public interface TrainerSummaryRepository extends MongoRepository<TrainerSummary, Long> {
    Optional<TrainerSummary> findByUsernameEquals(String username);
    List<TrainerSummary> getAllBy();
}
