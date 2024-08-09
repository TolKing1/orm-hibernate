package org.tolking.dto.converter.training;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.training.TrainingDTO;
import org.tolking.entity.Training;

@Component
public class TrainingConverter extends DTOConverter<Training, TrainingDTO> {

    public TrainingConverter(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<Training> getTypeEntity() {
        return Training.class;
    }

    @Override
    protected Class<TrainingDTO> getTypeDTO() {
        return TrainingDTO.class;
    }
}
