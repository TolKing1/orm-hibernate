package org.tolking.dto.trainer;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tolking.enums.TrainingsType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for creating an trainer")
public class TrainerCreateDTO {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("firstName")
    @NotNull(message = "FirstName can't be null")
    private String userFirstName;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("lastName")
    @NotNull(message = "LastName can't be null")
    private String userLastName;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("specialization")
    @NotNull(message = "Specialization can't be null")
    private TrainingsType trainingTypeName;
}
