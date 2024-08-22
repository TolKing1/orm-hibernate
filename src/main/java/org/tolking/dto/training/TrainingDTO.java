package org.tolking.dto.training;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for creating an training")
public class TrainingDTO {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "TrainerUsername can't be null")
    private String trainerUsername;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "TrainingName can't be null")
    private String trainingName;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Date can't be null")
    private Date date;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Duration can't be null")
    private Long duration;
}
