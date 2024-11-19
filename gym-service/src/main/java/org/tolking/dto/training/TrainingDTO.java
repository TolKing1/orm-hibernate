package org.tolking.dto.training;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for creating an training")
public class TrainingDTO {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "TrainerUsername can't be empty")
    private String trainerUsername;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "TrainingName can't be empty")
    private String trainingName;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Date can't be null")
    private LocalDate date;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @Positive(message = "Duration should be positive")
    private Long duration;
}
