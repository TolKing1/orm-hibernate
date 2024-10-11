package org.tolking.dto.converter.trainer;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.trainer.TrainerCreateDTO;
import org.tolking.entity.Trainer;

@Component
public class TrainerCreateConverter extends DTOConverter<Trainer, TrainerCreateDTO> {
    public TrainerCreateConverter(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<Trainer> getTypeEntity() {
        return Trainer.class;
    }

    @Override
    protected Class<TrainerCreateDTO> getTypeDTO() {
        return TrainerCreateDTO.class;
    }
}
