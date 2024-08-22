package org.tolking.dto.trainee;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for trainee update")
public class TraineeUpdateDTO {
    @JsonProperty("firstName")
    private String userFirstName;
    @JsonProperty("lastName")
    private String userLastName;
    private Date dateOfBirth;
    private String address;
    @JsonProperty("isActive")
    private boolean userIsActive;
}
