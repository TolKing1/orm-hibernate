package org.tolking.dto.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tolking.enums.TrainingsType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriteriaTraineeDTO {
    private LocalDate from;
    private LocalDate to;
    private String trainerUsername;
    private TrainingsType trainingType;
}
