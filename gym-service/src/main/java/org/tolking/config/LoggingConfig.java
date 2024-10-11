package org.tolking.config;

import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tolking.filter.LoggingFilter;

@Configuration
public class LoggingConfig {

    @Bean
    public Filter transactionIdFilter() {
        return new LoggingFilter();
    }
}
