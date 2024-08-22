package org.tolking.dto.trainer;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
    private String userFirstName;
    @JsonProperty("lastName")
    private String userLastName;
    @JsonProperty("isActive")
    private boolean userIsActive;
}
