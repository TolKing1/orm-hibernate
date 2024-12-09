package org.tolking.entity;


import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "trainerSummaries")
@Data
public class TrainerSummary {
    @MongoId
    private String username;

    @Field("firstName")
    @Indexed
    private String firstName;

    @Field("lastName")
    @Indexed
    private String lastName;

    @Field("isActive")
    private Boolean isActive;

    @Field("years")
    private Map<Integer, Map<Integer, Integer>> years = new HashMap<>();

    public void addTrainingDuration(LocalDate date, @Positive int duration) {
        int year = date.getYear();
        int month = date.getMonthValue();

        years.computeIfAbsent(year, k -> new HashMap<>())
                .merge(month, duration, Integer::sum);
    }

    public void removeTrainingDuration(LocalDate date, @Positive int duration) {
        int year = date.getYear();
        int month = date.getMonthValue();

        Map<Integer, Integer> months = years.get(year);
        if (months != null) {
            Integer currentDuration = months.get(month);
            if (currentDuration != null) {
                int newDuration = currentDuration - duration;
                if (newDuration > 0) {
                    months.put(month, newDuration);
                } else {
                    months.remove(month);
                    if (months.isEmpty()) {
                        years.remove(year);
                    }
                }
            }
        }
    }

}
