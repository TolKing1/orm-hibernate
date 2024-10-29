package org.tolking.training_event_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.tolking.training_event_service.enums.ActionType;

import java.util.Date;

@Data
public class TrainingEventDTO {
    @JsonProperty("username")
    private String trainerUserUsername;
    @JsonProperty("firstName")
    private String trainerUserFirstName;
    @JsonProperty("lastName")
    private String trainerUserLastName;
    @JsonProperty("isActive")
    private boolean trainerUserIsActive;
    private Long trainingId;
    @JsonProperty("trainingDate")
    private Date date;
    @JsonProperty("trainingDuration")
    private int duration;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;
}
