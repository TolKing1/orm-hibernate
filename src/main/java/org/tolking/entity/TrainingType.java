package org.tolking.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.tolking.enums.TrainingsType;

@Entity
@Data
@Table(name = "training_type")
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private TrainingsType name;
}
