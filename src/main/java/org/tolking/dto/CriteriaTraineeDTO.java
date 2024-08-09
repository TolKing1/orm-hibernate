package org.tolking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriteriaTraineeDTO {
    private Date from;
    private Date to;
    private String trainerUsername;
    private String trainingTypeName;
}
