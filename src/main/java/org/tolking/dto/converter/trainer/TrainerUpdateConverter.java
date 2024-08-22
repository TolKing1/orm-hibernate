package org.tolking.dto.converter.trainer;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.trainer.TrainerUpdateDTO;
import org.tolking.entity.Trainer;

@Component
public class TrainerUpdateConverter extends DTOConverter<Trainer, TrainerUpdateDTO> {
    public TrainerUpdateConverter(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<Trainer> getTypeEntity() {
        return Trainer.class;
    }

    @Override
    protected Class<TrainerUpdateDTO> getTypeDTO() {
        return TrainerUpdateDTO.class;
    }
}
