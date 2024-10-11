package org.tolking.training_event_service.config;

import org.modelmapper.Condition;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        Condition<?, ?> skipNulls = Conditions.isNotNull();

        modelMapper.getConfiguration().setPropertyCondition(skipNulls);

        return modelMapper;
    }
}
