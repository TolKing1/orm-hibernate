package org.tolking.dto.training;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingTraineeReadDTO {
    private long id;
    @JsonProperty("traineeUsername")
    private String trainerUserUsername;
    private String trainingName;
    @JsonProperty("trainingType")
    private String trainingTypeName;
    private LocalDate date;
    private Long duration;
}

