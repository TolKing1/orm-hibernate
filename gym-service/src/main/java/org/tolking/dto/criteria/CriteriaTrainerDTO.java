package org.tolking.dto.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriteriaTrainerDTO {
    private LocalDate from;
    private LocalDate to;
    private String traineeUsername;
}
