package org.tolking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.trainingType.TrainingTypeDTO;
import org.tolking.entity.TrainingType;
import org.tolking.enums.TrainingsType;
import org.tolking.exception.TrainingTypeNotFoundException;
import org.tolking.repository.TrainingTypeRepository;
import org.tolking.service.impl.TrainingTypeServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeServiceTest {
    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private DTOConverter<TrainingType, TrainingTypeDTO> converter;

    @Test
    public void create_shouldCreateTrainingType() {
        String typeName = "CARDIO";
        TrainingType trainingType = new TrainingType(typeName);

        // Act
        trainingTypeService.create(typeName);

        // Assert
        verify(trainingTypeRepository, times(1)).save(trainingType);
    }

    @Test
    public void findByName_shouldReturnTrainingType_whenFound() throws TrainingTypeNotFoundException {
        String typeName = "CARDIO";
        TrainingsType trainingsType = TrainingsType.valueOf(typeName);
        TrainingType trainingType = new TrainingType(typeName);

        when(trainingTypeRepository.getTrainingTypeByName(trainingsType)).thenReturn(Optional.of(trainingType));

        TrainingType result = trainingTypeService.findByName(trainingsType);

        assertEquals(trainingType, result);
        verify(trainingTypeRepository, times(1)).getTrainingTypeByName(trainingsType);
    }

    @Test
    public void findByName_shouldReturnTrainingType_whenNotFound() throws TrainingTypeNotFoundException {
        String typeName = "HIIT";
        TrainingsType trainingsType = TrainingsType.valueOf(typeName);

        when(trainingTypeRepository.getTrainingTypeByName(trainingsType)).thenReturn(Optional.empty());


        TrainingTypeNotFoundException exception = assertThrows(
                TrainingTypeNotFoundException.class,
                () -> trainingTypeService.findByName(trainingsType)
        );

        assertEquals("Can't find training type with name: HIIT", exception.getMessage());
        verify(trainingTypeRepository, times(1)).getTrainingTypeByName(trainingsType);
    }

    @Test
    public void getAll_shouldReturnTrainingTypeList() throws TrainingTypeNotFoundException {
        List<TrainingType> trainingTypeList = List.of(
                new TrainingType("CARDIO"),
                new TrainingType("HIIT")
        );


        when(trainingTypeRepository.getAllBy()).thenReturn(trainingTypeList);
        when(converter.toDtoList(trainingTypeList)).thenReturn(List.of(new TrainingTypeDTO()));

        List<TrainingTypeDTO> result = trainingTypeService.getAll();

        assertNotNull( result);
        assertEquals(1, result.size());

        verify(trainingTypeRepository, times(1)).getAllBy();
    }
}
