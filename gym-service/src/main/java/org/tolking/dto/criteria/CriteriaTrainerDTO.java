package org.tolking.dto.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriteriaTrainerDTO {
    private Date from;
    private Date to;
    private String traineeUsername;
}
