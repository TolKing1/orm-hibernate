package org.tolking.training_event_service.exception;

import java.time.LocalDateTime;

public record ApiErrorWithoutMessage(
        String path,
        String status,
        LocalDateTime localDateTime
) {
}
