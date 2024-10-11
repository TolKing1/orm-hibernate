package org.tolking.dto.converter.trainee;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.trainee.TraineeUpdateDTO;
import org.tolking.entity.Trainee;

@Component
public class TraineeUpdateConverter extends DTOConverter<Trainee, TraineeUpdateDTO> {
    public TraineeUpdateConverter(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<Trainee> getTypeEntity() {
        return Trainee.class;
    }

    @Override
    protected Class<TraineeUpdateDTO> getTypeDTO() {
        return TraineeUpdateDTO.class;
    }
}
