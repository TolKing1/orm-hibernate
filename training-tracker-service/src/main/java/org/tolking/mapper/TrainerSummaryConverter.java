package org.tolking.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.tolking.entity.TrainerSummary;
import org.tolking.external_dto.TrainingEventDTO;

@Component
@RequiredArgsConstructor
public class TrainerSummaryConverter extends DTOConverter<TrainerSummary, TrainingEventDTO> {

    @Override
    public TrainerSummary convertToEntity(TrainingEventDTO dto) {
        TrainerSummary trainerSummary = modelMapper.map(dto, TrainerSummary.class);

        trainerSummary.addTrainingDuration(dto.getDate(), dto.getDuration());
        return trainerSummary;
    }

    @Override
    protected Class<TrainerSummary> getTypeEntity() {
        return TrainerSummary.class;
    }

    @Override
    protected Class<TrainingEventDTO> getTypeDTO() {
        return TrainingEventDTO.class;
    }
}
