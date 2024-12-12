package org.tolking.integration.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.tolking.TrainingEventServiceApplication;

@CucumberContextConfiguration
@SpringBootTest(classes = TrainingEventServiceApplication.class)
@ActiveProfiles("test")
@Transactional
public class CucumberSpringConfiguration {
}