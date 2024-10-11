package org.tolking.dto.trainer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerForTraineeProfileDTO {
    @JsonProperty("username")
    private String userUsername;
    @JsonProperty("firstName")
    private String userFirstName;
    @JsonProperty( "lastName")
    private String userLastName;
    private String trainingTypeName;
}
