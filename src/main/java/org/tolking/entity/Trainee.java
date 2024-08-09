package org.tolking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
public class Trainee{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    private String address;

    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private User user;

    @OneToMany(mappedBy = "trainee", cascade = {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<Training> trainingList = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "trainer_trainee_m2m",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "trainee_id")
    )
    private List<Trainer> trainerList = new ArrayList<>();
}
