package org.tolking.dto.converter.trainer;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.trainer.TrainerProfileDTO;
import org.tolking.entity.Trainer;

@Component
public class TrainerProfileConverter extends DTOConverter<Trainer, TrainerProfileDTO> {
    public TrainerProfileConverter(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<Trainer> getTypeEntity() {
        return Trainer.class;
    }

    @Override
    protected Class<TrainerProfileDTO> getTypeDTO() {
        return TrainerProfileDTO.class;
    }
}
