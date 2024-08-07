package org.tolking.dto.converter.trainee;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.trainee.TraineeProfileDTO;
import org.tolking.entity.Trainee;

@Component
public class TraineeProfileConverter extends DTOConverter<Trainee, TraineeProfileDTO> {
    public TraineeProfileConverter(ModelMapper modelMapper) {
        super(modelMapper);
    }

    public Trainee updateEntity(Trainee trainee, TraineeProfileDTO trainerDTO){
        modelMapper.map(trainerDTO, trainee);

        return trainee;
    }

    @Override
    protected Class<Trainee> getTypeEntity() {
        return Trainee.class;
    }

    @Override
    protected Class<TraineeProfileDTO> getTypeDTO() {
        return TraineeProfileDTO.class;
    }
}
