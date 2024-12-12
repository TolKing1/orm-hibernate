package org.tolking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "training_type_id")
    @ToStringExclude
    private TrainingType trainingType;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "trainer_trainee_m2m",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "trainee_id")
    )
    @ToStringExclude
    private List<Trainee> traineeList = new ArrayList<>();

    @OneToMany(mappedBy = "trainer", cascade = {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @Exclude
    private List<Training> trainingList = new ArrayList<>();
}
