package org.tolking.training_event_service.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TrainerSummary {
    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private Map<Integer, Map<Integer, Integer>> years = new HashMap<>();

    public TrainerSummary(String username, String firstName, String lastName, boolean isActive) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
    }

    public void addTrainingDuration(int year, int month, int duration) {
        years.computeIfAbsent(year, k -> new HashMap<>())
                .merge(month, duration, Integer::sum);
    }
}
