package org.tolking.dto.converter.training;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.training.TrainingTrainerReadDTO;
import org.tolking.entity.Training;

@Component
public class TrainingTrainerReadConverter extends DTOConverter<Training, TrainingTrainerReadDTO> {
    public TrainingTrainerReadConverter(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<Training> getTypeEntity() {
        return Training.class;
    }

    @Override
    protected Class<TrainingTrainerReadDTO> getTypeDTO() {
        return TrainingTrainerReadDTO.class;
    }
}
