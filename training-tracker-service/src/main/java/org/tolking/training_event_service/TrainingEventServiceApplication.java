package org.tolking.training_event_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TrainingEventServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainingEventServiceApplication.class, args);
    }

}
