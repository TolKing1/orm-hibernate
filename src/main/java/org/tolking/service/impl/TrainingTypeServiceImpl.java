package org.tolking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.tolking.dao.TrainingTypeDAO;
import org.tolking.entity.TrainingType;
import org.tolking.enums.TrainingsType;
import org.tolking.exception.TrainingTypeNotFoundException;


@Service
@RequiredArgsConstructor
@Log
public class TrainingTypeServiceImpl implements org.tolking.service.TrainingTypeService {
    private final TrainingTypeDAO trainingTypeDao;

    @Override
    public void create(String name){
        TrainingType trainingType = new TrainingType(name);

        trainingTypeDao.create(trainingType);
        log.info("Created type: %s".formatted(name));
    }

    @Override
    public TrainingType findByName(String name) throws TrainingTypeNotFoundException {
        TrainingsType type = TrainingsType.valueOf(name);

        return trainingTypeDao.readByName(type).orElseThrow(
                ()-> new TrainingTypeNotFoundException(name)
        );
    }


}
