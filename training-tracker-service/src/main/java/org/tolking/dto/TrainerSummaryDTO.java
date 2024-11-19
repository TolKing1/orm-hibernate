package org.tolking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TrainerSummaryDTO {
    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;
    @Schema(
            description = "Training duration records, structured as a map. Keys of the outer map are years, and values are maps containing months as keys (1-12).",
            example = "{\"2023\": {\"1\": 20, \"2\": 15, \"3\": 30}, \"2024\": {\"1\": 10, \"2\": 25}}"
    )
    private Map<Integer, Map<Integer, Integer>> years = new HashMap<>();
}
