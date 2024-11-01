package org.tolking.messaging;

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

    @Value("${spring.activemq.event-queue-name}")
    private String trackerEventQueue;

    public void sendEvent(TrainingEventDTO dto) {
        log.debug("Sending message from queue {} : {}", trackerEventQueue, dto);
        try {

            jmsTemplate.convertAndSend(trackerEventQueue, dto, message -> {
                message.setStringProperty(CORRELATION_ID, MDC.get(CORRELATION_ID));
                return message;
            });
        } catch (JmsException e) {
            log.warn("Error sending message to queue {} : {}", trackerEventQueue, e.getMessage());
        }
    }
}
