package org.tolking.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.tolking.service.TrainingEventService;
import org.tolking.external_dto.TrainingEventDTO;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrackerEventConsumer {
    private final TrainingEventService trainingEventService;
    private final static String QUEUE_NAME = "training-event-queue";

    @JmsListener(destination = QUEUE_NAME)
    public void receiveMessage(TrainingEventDTO trainingEventDTO) {
        log.debug("Received message from queue {} : {}", QUEUE_NAME, trainingEventDTO);
        trainingEventService.create(trainingEventDTO);
    }
}
