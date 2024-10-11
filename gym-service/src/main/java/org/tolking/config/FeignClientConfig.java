package org.tolking.config;

import feign.RequestInterceptor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    private static final String CORRELATION_ID_HEADER_NAME = "X-Correlation-Id";

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            String correlationId = MDC.get(CORRELATION_ID_HEADER_NAME);
            if (correlationId != null) {
                template.header(CORRELATION_ID_HEADER_NAME, correlationId);
            }
        };
    }
}
