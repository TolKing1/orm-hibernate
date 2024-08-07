package org.tolking.dto.converter.trainee;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.trainee.TraineeDTO;
import org.tolking.entity.Trainee;

@Component
public class TraineeConverter extends DTOConverter<Trainee, TraineeDTO> {
    public TraineeConverter(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<Trainee> getTypeEntity() {
        return Trainee.class;
    }

    @Override
    protected Class<TraineeDTO> getTypeDTO() {
        return TraineeDTO.class;
    }
}
