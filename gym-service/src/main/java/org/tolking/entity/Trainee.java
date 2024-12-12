package org.tolking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    private String address;

    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private User user;

    @OneToMany(mappedBy = "trainee", cascade = {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @Exclude
    private List<Training> trainingList = new ArrayList<>();

    @ManyToMany(mappedBy = "traineeList", fetch = FetchType.EAGER)
    @Exclude
    private List<Trainer> trainerList = new ArrayList<>();

    @PreUpdate
    private void updateAssociations() {
        for (Trainer trainer : this.trainerList) {
            trainer.getTraineeList().add(this);
        }
    }

    @PreRemove
    private void removeAssociations() {
        for (Trainer trainer : this.trainerList) {
            trainer.getTraineeList().remove(this);
        }
    }
}
