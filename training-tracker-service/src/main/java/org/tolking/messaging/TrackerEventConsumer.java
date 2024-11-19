package org.tolking.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.tolking.external_dto.TrainingEventDTO;
import org.tolking.service.TrainingEventService;

import static org.tolking.filter.LoggingFilter.CORRELATION_ID;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrackerEventConsumer {
    private final TrainingEventService trainingEventService;
    private final ObjectMapper objectMapper;
    private final static String QUEUE_NAME = "training-event-queue";

    @JmsListener(destination = QUEUE_NAME)
    public void receiveMessage(String json, @Header(CORRELATION_ID) String correlationId) {
        addTraceId(correlationId);

        try {
            TrainingEventDTO trainingEventDTO = objectMapper.readValue(json, TrainingEventDTO.class);
            log.debug("Received message from queue {}: {}", QUEUE_NAME, trainingEventDTO);
            trainingEventService.create(trainingEventDTO);
        } catch (Exception e) {
            log.error("Error processing message from queue {}: {}", QUEUE_NAME, json, e);
        }
    }

    private static void addTraceId(String correlationId) {
        MDC.put(CORRELATION_ID, correlationId);
    }
}
