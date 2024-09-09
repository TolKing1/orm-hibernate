package org.tolking.dto.trainee;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
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
    @Size(min = 3, max = 50 , message = "FirstName's length should be between 3 and 50")
    private String userFirstName;

    @JsonProperty("lastName")
    @Size(min = 3, max = 50 , message = "LastName's length should be between 3 and 50")
    private String userLastName;

    private Date dateOfBirth;
    private String address;

    @JsonProperty("isActive")
    private Boolean userIsActive;
}
