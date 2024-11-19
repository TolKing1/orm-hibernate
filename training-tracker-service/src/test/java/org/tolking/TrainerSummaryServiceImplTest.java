package org.tolking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.tolking.dto.TrainerSummaryDTO;
import org.tolking.entity.TrainerSummary;
import org.tolking.enums.ActionType;
import org.tolking.external_dto.TrainingEventDTO;
import org.tolking.mapper.TrainerSummaryConverter;
import org.tolking.repository.TrainerSummaryRepository;
import org.tolking.service.impl.TrainingEventServiceImpl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingEventServiceImplTest {

    @Mock
    private TrainerSummaryRepository trainerSummaryRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private TrainerSummaryConverter trainerSummaryConverter;

    @InjectMocks
    private TrainingEventServiceImpl trainingEventServiceImpl;

    private TrainingEventDTO trainingEventDTO;
    private TrainerSummary trainerSummary;

    @BeforeEach
    void setUp() {
        trainingEventDTO = new TrainingEventDTO();
        trainingEventDTO.setUsername("trainer123");
        trainingEventDTO.setDuration(2);
        trainingEventDTO.setDate(LocalDate.of(2023, 10, 1));
        trainingEventDTO.setActionType(ActionType.ADD);

        trainerSummary = new TrainerSummary();
        trainerSummary.setUsername("trainer123");
    }

    @Test
    void create_whenTrainerSummaryExists_addsDurationAndSaves() {
        when(trainerSummaryRepository.findByUsernameEquals("trainer123")).thenReturn(Optional.of(trainerSummary));
        when(trainerSummaryRepository.save(trainerSummary)).thenReturn(trainerSummary);

        trainingEventServiceImpl.create(trainingEventDTO);

        verify(trainerSummaryRepository).save(any(TrainerSummary.class));
        verify(trainerSummaryConverter, never()).convertToEntity(any(TrainingEventDTO.class));
    }

    @Test
    void create_whenTrainerSummaryNotExists_convertsAndSaves() {
        when(trainerSummaryRepository.findByUsernameEquals("trainer123")).thenReturn(Optional.empty());
        when(trainerSummaryConverter.convertToEntity(trainingEventDTO)).thenReturn(trainerSummary);
        when(trainerSummaryRepository.save(trainerSummary)).thenReturn(trainerSummary);

        trainingEventServiceImpl.create(trainingEventDTO);

        verify(trainerSummaryRepository).save(trainerSummary);
        verify(trainerSummaryConverter).convertToEntity(trainingEventDTO);
    }

    @Test
    void getAllSummary_fetchesAndMapsSummaries() {
        TrainerSummaryDTO trainerSummaryDTO = new TrainerSummaryDTO();
        when(trainerSummaryRepository.getAllBy()).thenReturn(Collections.singletonList(trainerSummary));
        when(modelMapper.map(any(TrainerSummary.class), eq(TrainerSummaryDTO.class))).thenReturn(trainerSummaryDTO);

        List<TrainerSummaryDTO> result = trainingEventServiceImpl.getAllSummary();

        assertEquals(1, result.size());
        verify(trainerSummaryRepository).getAllBy();
        verify(modelMapper).map(trainerSummary, TrainerSummaryDTO.class);
    }
}