package org.tolking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.trainingType.TrainingTypeDTO;
import org.tolking.entity.TrainingType;
import org.tolking.enums.TrainingsType;
import org.tolking.exception.TrainingTypeNotFoundException;
import org.tolking.repository.TrainingTypeRepository;
import org.tolking.service.TrainingTypeService;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;
    private final DTOConverter<TrainingType, TrainingTypeDTO> converter;

    @Override
    public void create(String name) {
        log.info("Creating new TrainingType with name: {}", name);
        TrainingType trainingType = new TrainingType(name);
        trainingTypeRepository.save(trainingType);
        log.info("TrainingType created successfully with name: {}", name);
    }

    @Override
    public List<TrainingTypeDTO> getAll() {
        log.info("Fetching all TrainingTypes");
        return converter.toDtoList(trainingTypeRepository.getAllBy());
    }

    @Override
    public TrainingType findByName(TrainingsType name) throws TrainingTypeNotFoundException {
        log.info("Finding TrainingType with name: {}", name);
        return trainingTypeRepository.getTrainingTypeByName(name)
                .orElseThrow(() -> new TrainingTypeNotFoundException(name.toString()));
    }
}