package org.tolking;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.tolking.dao.TrainingTypeDAO;
import org.tolking.entity.TrainingType;
import org.tolking.enums.TrainingsType;
import org.tolking.exception.TrainingTypeNotFoundException;
import org.tolking.service.impl.TrainingTypeServiceImpl;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class TrainingTypeServiceTest {
    @Mock
    private TrainingTypeDAO trainingTypeDao;

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void create_shouldCreateTrainingType() {
        String typeName = "CARDIO";
        TrainingType trainingType = new TrainingType(typeName);

        // Act
        trainingTypeService.create(typeName);

        // Assert
        verify(trainingTypeDao, times(1)).create(trainingType);
    }

    @Test
    public void findByName_shouldReturnTrainingType_whenFound() throws TrainingTypeNotFoundException {
        String typeName = "CARDIO";
        TrainingsType trainingsType = TrainingsType.valueOf(typeName);
        TrainingType trainingType = new TrainingType(typeName);

        when(trainingTypeDao.readByName(trainingsType)).thenReturn(Optional.of(trainingType));

        // Act
        TrainingType result = trainingTypeService.findByName(typeName);

        // Assert
        assertEquals(trainingType, result);
        verify(trainingTypeDao, times(1)).readByName(trainingsType);
    }

    @Test
    public void findByName_shouldReturnTrainingType_whenNotFound() throws TrainingTypeNotFoundException {
        String typeName = "HIIT";
        TrainingsType trainingsType = TrainingsType.valueOf(typeName);

        when(trainingTypeDao.readByName(trainingsType)).thenReturn(Optional.empty());


        TrainingTypeNotFoundException exception = assertThrows(
                TrainingTypeNotFoundException.class,
                () -> trainingTypeService.findByName(typeName)
        );

        assertEquals("Can't find training type with name: HIIT", exception.getMessage());
        verify(trainingTypeDao, times(1)).readByName(trainingsType);
    }
}
