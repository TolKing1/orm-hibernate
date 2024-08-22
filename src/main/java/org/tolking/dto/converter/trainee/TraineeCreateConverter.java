package org.tolking.dto.converter.trainee;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.trainee.TraineeCreateDTO;
import org.tolking.entity.Trainee;

@Component
public class TraineeCreateConverter extends DTOConverter<Trainee, TraineeCreateDTO> {
    public TraineeCreateConverter(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<Trainee> getTypeEntity() {
        return Trainee.class;
    }

    @Override
    protected Class<TraineeCreateDTO> getTypeDTO() {
        return TraineeCreateDTO.class;
    }
}
