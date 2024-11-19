package org.tolking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.tolking.dto.TrainerSummaryDTO;
import org.tolking.entity.TrainerSummary;
import org.tolking.enums.ActionType;
import org.tolking.external_dto.TrainingEventDTO;
import org.tolking.mapper.TrainerSummaryConverter;
import org.tolking.repository.TrainerSummaryRepository;
import org.tolking.service.TrainingEventService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingEventServiceImpl implements TrainingEventService {
    private final TrainerSummaryRepository trainerSummaryRepository;
    private final ModelMapper modelMapper;
    private final TrainerSummaryConverter trainerSummaryConverter;

    @Override
    public void create(TrainingEventDTO trainingEventDTO) {
        log.debug("Creating event with DTO: {} ", trainingEventDTO);
        TrainerSummary trainerSummary = getTrainerSummary(trainingEventDTO);
        TrainerSummary createdEvent = trainerSummaryRepository.save(trainerSummary);
        log.debug("Event created successfully with ID: {}", createdEvent.getUsername());
    }

    private TrainerSummary getTrainerSummary(TrainingEventDTO trainingEventDTO) {
        return trainerSummaryRepository.findByUsernameEquals(trainingEventDTO.getTrainerUserUsername())
                .map(existingSummary -> updateDuration(trainingEventDTO, existingSummary))
                .orElseGet(() -> trainerSummaryConverter.convertToEntity(trainingEventDTO));
    }

    private TrainerSummary updateDuration(TrainingEventDTO trainingEventDTO, TrainerSummary trainerSummary) {
        int duration = trainingEventDTO.getDuration();
        LocalDate date = trainingEventDTO.getDate();

        trainerSummary.setIsActive(trainingEventDTO.getTrainerUserIsActive());

        if (trainingEventDTO.getActionType().equals(ActionType.ADD)) {
            trainerSummary.addTrainingDuration(date, duration);
        } else {
            trainerSummary.removeTrainingDuration(date, duration);
        }
        return trainerSummary;
    }

    @Override
    public List<TrainerSummaryDTO> getAllSummary() {
        log.debug("Fetching summary of event");

        List<TrainerSummary> trainingSummaries = trainerSummaryRepository.getAllBy();


        return trainingSummaries.stream()
                .map(
                trainerSummary -> modelMapper.map(trainerSummary, TrainerSummaryDTO.class)
                ).toList();
    }
}
