package org.tolking.dto.training;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingTraineeReadDTO {
    @JsonProperty("traineeUsername")
    private String trainerUserUsername;
    private String trainingName;
    @JsonProperty("trainingType")
    private String trainingTypeName;
    private Date date;
    private Long duration;
}

