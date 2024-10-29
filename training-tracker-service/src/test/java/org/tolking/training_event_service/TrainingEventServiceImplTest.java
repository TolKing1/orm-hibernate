package org.tolking.training_event_service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.tolking.training_event_service.dto.TrainingEventDTO;
import org.tolking.training_event_service.entity.TrainingEvent;
import org.tolking.training_event_service.enums.ActionType;
import org.tolking.training_event_service.model.TrainerSummary;
import org.tolking.training_event_service.repository.TrainerRepository;
import org.tolking.training_event_service.service.impl.TrainingEventServiceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingEventServiceImplTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TrainingEventServiceImpl trainingEventService;

    @Test
    void testCreate() {
        TrainingEventDTO trainingEventDTO = new TrainingEventDTO();
        trainingEventDTO.setTrainerUserUsername("johndoe");
        trainingEventDTO.setTrainerUserFirstName("John");
        trainingEventDTO.setTrainerUserLastName("Doe");
        trainingEventDTO.setTrainerUserIsActive(true);
        trainingEventDTO.setDate(new Date());
        trainingEventDTO.setDuration(2);
        trainingEventDTO.setActionType(ActionType.ADD);

        TrainingEvent trainingEvent = new TrainingEvent();
        trainingEvent.setId(1L);
        trainingEvent.setUsername("johndoe");
        trainingEvent.setFirstName("John");
        trainingEvent.setLastName("Doe");
        trainingEvent.setActive(true);
        trainingEvent.setTrainingDate(new Date());
        trainingEvent.setTrainingDuration(2);
        trainingEvent.setActionType(ActionType.ADD);

        when(modelMapper.map(trainingEventDTO, TrainingEvent.class)).thenReturn(trainingEvent);
        when(trainerRepository.save(trainingEvent)).thenReturn(trainingEvent);

        trainingEventService.create(trainingEventDTO);

        verify(modelMapper, times(1)).map(trainingEventDTO, TrainingEvent.class);
        verify(trainerRepository, times(1)).save(trainingEvent);
    }

    @Test
    void testGetAllSummary() {
        TrainingEvent trainingEvent1 = new TrainingEvent();
        trainingEvent1.setUsername("johndoe");
        trainingEvent1.setFirstName("John");
        trainingEvent1.setLastName("Doe");
        trainingEvent1.setActive(true);
        trainingEvent1.setTrainingDate(new Date());
        trainingEvent1.setTrainingDuration(2);
        trainingEvent1.setActionType(ActionType.ADD);

        TrainingEvent trainingEvent2 = new TrainingEvent();
        trainingEvent2.setUsername("janedoe");
        trainingEvent2.setFirstName("Jane");
        trainingEvent2.setLastName("Doe");
        trainingEvent2.setActive(true);
        trainingEvent2.setTrainingDate(new Date());
        trainingEvent2.setTrainingDuration(3);
        trainingEvent2.setActionType(ActionType.ADD);

        List<TrainingEvent> trainingEvents = Arrays.asList(trainingEvent1, trainingEvent2);

        when(trainerRepository.findAllAddEventsWithoutDelete()).thenReturn(trainingEvents);

        List<TrainerSummary> summaries = trainingEventService.getAllSummary();

        assertEquals(2, summaries.size());

        TrainerSummary summary1 = summaries.stream().filter(s -> s.getUsername().equals("johndoe")).findFirst().orElse(null);
        TrainerSummary summary2 = summaries.stream().filter(s -> s.getUsername().equals("janedoe")).findFirst().orElse(null);

        assertNotNull(summary1);
        assertNotNull(summary2);

        assertEquals("John", summary1.getFirstName());
        assertEquals("Jane", summary2.getFirstName());

        verify(trainerRepository, times(1)).findAllAddEventsWithoutDelete();
    }



}
