package org.tolking.dto.converter.training;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.trainingType.TrainingTypeDTO;
import org.tolking.entity.TrainingType;

@Component
public class TrainingTypeNameConverter extends DTOConverter<TrainingType, TrainingTypeDTO> {
    public TrainingTypeNameConverter(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<TrainingType> getTypeEntity() {
        return TrainingType.class;
    }

    @Override
    protected Class<TrainingTypeDTO> getTypeDTO() {
        return TrainingTypeDTO.class;
    }
}
