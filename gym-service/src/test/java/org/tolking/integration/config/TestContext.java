package org.tolking.integration.config;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class TestContext {
    private String jwtToken;
}