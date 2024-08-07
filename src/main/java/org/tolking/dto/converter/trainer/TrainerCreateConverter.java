package org.tolking.dto.converter.trainer;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.trainer.TrainerDTO;
import org.tolking.entity.Trainer;

@Component
public class TrainerCreateConverter extends DTOConverter<Trainer, TrainerDTO> {
    public TrainerCreateConverter(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<Trainer> getTypeEntity() {
        return Trainer.class;
    }

    @Override
    protected Class<TrainerDTO> getTypeDTO() {
        return TrainerDTO.class;
    }
}
