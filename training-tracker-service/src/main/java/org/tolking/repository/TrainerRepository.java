package org.tolking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.tolking.entity.TrainingEvent;

import java.util.List;

@Repository
public interface TrainerRepository extends JpaRepository<TrainingEvent, Long> {
    @Query("SELECT te FROM TrainingEvent te WHERE te.actionType = 'ADD' AND te.trainingId NOT IN (SELECT te2.trainingId FROM TrainingEvent te2 WHERE te2.actionType = 'DELETE')")
    List<TrainingEvent> findAllAddEventsWithoutDelete();
}
