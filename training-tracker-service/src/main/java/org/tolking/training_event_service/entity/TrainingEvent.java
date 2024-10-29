package org.tolking.training_event_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.tolking.training_event_service.enums.ActionType;

import java.util.Date;

@Entity
@Data
public class TrainingEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private Long trainingId;
    @Column(nullable = false)
    private Date trainingDate;
    private int trainingDuration;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

}
