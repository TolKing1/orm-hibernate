package org.tolking.integration.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.tolking.GymApplication;

@CucumberContextConfiguration
@SpringBootTest(classes = GymApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CucumberSpringConfiguration {
}

