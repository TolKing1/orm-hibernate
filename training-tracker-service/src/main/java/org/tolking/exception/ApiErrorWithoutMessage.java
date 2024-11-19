package org.tolking.exception;

import java.time.LocalDateTime;

public record ApiErrorWithoutMessage(
        String path,
        String status,
        LocalDateTime localDateTime
) {
}
