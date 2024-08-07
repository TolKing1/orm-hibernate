package org.tolking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
public class Trainer{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "training_type_id")
    private TrainingType trainingType;

    @OneToOne(cascade = {CascadeType.PERSIST})
    private User user;

    @ManyToMany
    @JoinTable(
        name = "trainer_trainee_m2m",
            joinColumns = @JoinColumn,
            inverseJoinColumns = @JoinColumn(name = "trainee_id")
    )
    private List<Trainee> traineeList = new ArrayList<>();
}
