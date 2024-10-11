package org.tolking.controller.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tolking.config.FeignClientConfig;
import org.tolking.external_dto.TrainingEventDTO;

@FeignClient(name = "training-tracker-service", configuration = FeignClientConfig.class)
public interface TrainingEventClient{
    @RequestMapping("/event")
    void createEvent(@RequestBody TrainingEventDTO trainingEventDTO);
}
