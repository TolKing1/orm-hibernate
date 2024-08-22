package org.tolking.dto.converter.training;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.training.TrainingTraineeReadDTO;
import org.tolking.entity.Training;

@Component
public class TrainingTraineeReadConverter extends DTOConverter<Training, TrainingTraineeReadDTO> {
    public TrainingTraineeReadConverter(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<Training> getTypeEntity() {
        return Training.class;
    }

    @Override
    protected Class<TrainingTraineeReadDTO> getTypeDTO() {
        return TrainingTraineeReadDTO.class;
    }
}
