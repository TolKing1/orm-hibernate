package org.tolking.dto.training;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingDTO {
    @NonNull
    private Long trainerId;
    @NonNull
    private String trainingName;
    @NonNull
    private Integer trainingTypeId;
    @NonNull
    private Date date;
    @NonNull
    private Long duration;
}
