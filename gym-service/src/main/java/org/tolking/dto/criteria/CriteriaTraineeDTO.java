package org.tolking.dto.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tolking.enums.TrainingsType;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriteriaTraineeDTO {
    private Date from;
    private Date to;
    private String trainerUsername;
    private TrainingsType trainingType;
}
