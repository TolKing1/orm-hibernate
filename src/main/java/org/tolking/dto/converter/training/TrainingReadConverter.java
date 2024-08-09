package org.tolking.dto.converter.training;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.training.TrainingReadDTO;
import org.tolking.entity.Training;

@Component
public class TrainingReadConverter extends DTOConverter<Training, TrainingReadDTO> {
    public TrainingReadConverter(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<Training> getTypeEntity() {
        return Training.class;
    }

    @Override
    protected Class<TrainingReadDTO> getTypeDTO() {
        return TrainingReadDTO.class;
    }
}
