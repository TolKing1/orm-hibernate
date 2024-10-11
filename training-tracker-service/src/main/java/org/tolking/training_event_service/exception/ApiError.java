package org.tolking.training_event_service.exception;

import java.time.LocalDateTime;

public record ApiError(
        String path,
        String message,
        String status,
        LocalDateTime localDateTime
){}
