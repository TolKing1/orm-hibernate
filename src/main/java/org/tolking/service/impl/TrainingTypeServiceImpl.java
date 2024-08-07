package org.tolking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tolking.dao.CR;
import org.tolking.entity.TrainingType;
import org.tolking.enums.TrainingsType;
import org.tolking.exception.TrainingTypeNotFoundException;


@Service
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements org.tolking.service.TrainingTypeService {
    public static final String NAME_PARAM = "name";
    private final CR<TrainingType> trainingTypeDao;

    @Override
    public void create(String name){
        TrainingType trainingType = new TrainingType(name);

        trainingTypeDao.create(trainingType);
    }

    @Override
    public TrainingType findByName(String name) throws TrainingTypeNotFoundException {
        TrainingsType type = TrainingsType.valueOf(name);

        return trainingTypeDao.readByParam(NAME_PARAM, type, TrainingType.class).orElseThrow(
                ()-> new TrainingTypeNotFoundException(name)
        );
    }


}
