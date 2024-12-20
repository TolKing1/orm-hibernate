package org.tolking.dto.trainer;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(min = 3, max = 50, message = "FirstName's length should be between 3 and 50")
    private String userFirstName;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("lastName")
    @Size(min = 3, max = 50, message = "LastName's length should be between 3 and 50")
    private String userLastName;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("specialization")
    @NotNull(message = "Specialization can't be null")
    private TrainingsType trainingTypeName;
}
