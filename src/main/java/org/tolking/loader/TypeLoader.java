package org.tolking.loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.tolking.enums.TrainingsType;
import org.tolking.exception.TrainingTypeNotFoundException;
import org.tolking.service.TrainingTypeService;

@Component
@RequiredArgsConstructor
@Log
public class TypeLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final TrainingTypeService trainingTypeService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        TrainingsType[] values = TrainingsType.values();

        for (TrainingsType value : values) {
            String stringValue = value.name();

            try {
                trainingTypeService.findByName(stringValue);
                log.warning("Already exists: %s".formatted(stringValue));
            } catch (TrainingTypeNotFoundException e) {
                trainingTypeService.create(stringValue);
                log.info("Created type: %s".formatted(stringValue));
            }
        }
    }
}
