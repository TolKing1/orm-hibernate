package org.tolking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tolking.enums.TrainingsType;

@Entity
@Data
@Table(name = "training_type")
@NoArgsConstructor
public class TrainingType {
    public TrainingType(String name) {
        this.name = TrainingsType.valueOf(name);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainingsType name;

}
