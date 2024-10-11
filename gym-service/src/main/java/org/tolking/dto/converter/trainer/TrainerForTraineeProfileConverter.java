package org.tolking.dto.converter.trainer;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.trainer.TrainerForTraineeProfileDTO;
import org.tolking.entity.Trainer;

@Component
public class TrainerForTraineeProfileConverter extends DTOConverter<Trainer, TrainerForTraineeProfileDTO> {
    public TrainerForTraineeProfileConverter(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<Trainer> getTypeEntity() {
        return Trainer.class;
    }

    @Override
    protected Class<TrainerForTraineeProfileDTO> getTypeDTO() {
        return TrainerForTraineeProfileDTO.class;
    }
}
