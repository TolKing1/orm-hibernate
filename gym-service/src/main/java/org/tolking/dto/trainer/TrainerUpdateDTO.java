package org.tolking.dto.trainer;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for trainer update")
public class TrainerUpdateDTO {
    @JsonProperty("firstName")
    @Size(min = 3, max = 50, message = "FirstName's length should be between 3 and 50")
    private String userFirstName;

    @JsonProperty("lastName")
    @Size(min = 3, max = 50, message = "LastName's length should be between 3 and 50")
    private String userLastName;

    @JsonProperty("isActive")
    private Boolean userIsActive;
}
