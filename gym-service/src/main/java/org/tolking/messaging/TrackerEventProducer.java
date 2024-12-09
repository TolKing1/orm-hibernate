package org.tolking.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.tolking.external_dto.TrainingEventDTO;

import static org.tolking.filter.LoggingFilter.CORRELATION_ID;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrackerEventProducer {
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.activemq.event-queue-name}")
    private String trackerEventQueue;

    public void sendEvent(TrainingEventDTO dto) {
        log.debug("Sending message from queue {} : {}", trackerEventQueue, dto);
        try {
            String json = objectMapper.writeValueAsString(dto);
            jmsTemplate.convertAndSend(trackerEventQueue, json, message -> {
                message.setStringProperty(CORRELATION_ID, MDC.get(CORRELATION_ID));
                return message;
            });
        } catch (JmsException |JsonProcessingException e) {
            log.warn("Error sending message to queue {} : {}", trackerEventQueue, e.getMessage(), e);
        }
    }
}
