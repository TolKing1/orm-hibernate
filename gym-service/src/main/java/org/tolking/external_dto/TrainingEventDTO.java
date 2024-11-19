package org.tolking.external_dto;

import lombok.Data;
import org.tolking.enums.ActionType;

import java.time.LocalDate;

@Data
public class TrainingEventDTO {
    private String trainerUserUsername;

    private String trainerUserFirstName;

    private String trainerUserLastName;

    private Boolean trainerUserIsActive;

    private LocalDate date;

    private int duration;

    private ActionType actionType;
}
