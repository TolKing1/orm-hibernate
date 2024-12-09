package org.tolking.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.tolking.entity.TrainerSummary;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerSummaryRepository extends MongoRepository<TrainerSummary, Long> {
    Optional<TrainerSummary> findByUsernameEquals(String username);
    List<TrainerSummary> getAllBy();
}
