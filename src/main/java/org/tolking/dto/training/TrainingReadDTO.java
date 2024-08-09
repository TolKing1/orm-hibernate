package org.tolking.dto.training;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingReadDTO {
    private String trainerUserUsername;
    private String traineeUserUsername;
    private String trainingName;
    private String trainingTypeName;
    private Date date;
    private Long duration;
}

