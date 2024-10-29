package org.tolking.training_event_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.tolking.training_event_service.dto.TrainingEventDTO;
import org.tolking.training_event_service.entity.TrainingEvent;
import org.tolking.training_event_service.model.TrainerSummary;
import org.tolking.training_event_service.repository.TrainerRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingEventServiceImpl implements org.tolking.training_event_service.service.TrainingEventService {
    private final TrainerRepository trainerRepository;
    private final ModelMapper modelMapper;

    private static void addTrainingToMap(TrainingEvent trainingEvent, TrainerSummary summary, YearMonth yearMonth, Map<String, TrainerSummary> summaryMap, String username) {
        summary.addTrainingDuration(yearMonth.getYear(), yearMonth.getMonthValue(), trainingEvent.getTrainingDuration());

        summaryMap.put(username, summary);
    }

    private static YearMonth getYearMonth(TrainingEvent trainingEvent) {
        Date trainingDate = trainingEvent.getTrainingDate();
        LocalDate localDate = trainingDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return YearMonth.from(localDate);
    }

    @Override
    public void create(TrainingEventDTO trainingEventDTO) {
        log.debug("Creating event with DTO: {} ", trainingEventDTO);
        TrainingEvent trainingEvent = modelMapper.map(trainingEventDTO, TrainingEvent.class);

        TrainingEvent createdEvent = trainerRepository.save(trainingEvent);
        log.debug("Event created successfully with ID: {}", createdEvent.getId());
    }

    @Override
    public List<TrainerSummary> getAllSummary() {
        log.debug("Fetching summary of event");

        List<TrainingEvent> trainingEvents = trainerRepository.findAllAddEventsWithoutDelete();

        Map<String, TrainerSummary> summaryMap = new HashMap<>();

        for (TrainingEvent trainingEvent : trainingEvents) {
            String username = trainingEvent.getUsername();
            TrainerSummary summary = summaryMap.getOrDefault(username, new TrainerSummary(
                    trainingEvent.getUsername(),
                    trainingEvent.getFirstName(),
                    trainingEvent.getLastName(),
                    trainingEvent.isActive()
            ));

            YearMonth yearMonth = getYearMonth(trainingEvent);

            addTrainingToMap(trainingEvent, summary, yearMonth, summaryMap, username);
        }

        return new ArrayList<>(summaryMap.values());
    }
}
