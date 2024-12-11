package org.tolking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToStringExclude
    private Trainer trainer;

    @ManyToOne
    @ToStringExclude
    private Trainee trainee;

    @Column(name = "training_name", nullable = false)
    private String trainingName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_type_id")
    @Exclude
    private TrainingType trainingType;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    @Positive
    private long duration;

    private boolean isDeleted;

    private LocalDate deleteDate;


}
