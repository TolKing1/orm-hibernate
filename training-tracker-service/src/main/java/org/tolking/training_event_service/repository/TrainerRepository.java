package org.tolking.training_event_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tolking.training_event_service.entity.TrainingEvent;
import org.tolking.training_event_service.enums.ActionType;

import java.util.List;

@Repository
public interface TrainerRepository extends JpaRepository<TrainingEvent, Long> {
    List<TrainingEvent> findAllByActionType(ActionType actionType);
}
