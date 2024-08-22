package org.tolking.dto.trainee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeForTrainerProfileDTO {
    @JsonProperty("firstName")
    private String userFirstName;
    @JsonProperty("lastName")
    private String userLastName;
    @JsonProperty("username")
    private String userUsername;
}
