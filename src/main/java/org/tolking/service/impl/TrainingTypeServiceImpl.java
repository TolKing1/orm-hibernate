package org.tolking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tolking.dao.CR;
import org.tolking.entity.TrainingType;
import org.tolking.exception.TrainingTypeNotFoundException;


@Service
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements org.tolking.service.TrainingTypeService {
    public static final String NAME_PARAM = "name";
    private final CR<TrainingType> trainingTypeDao;

    @Override
    public void create(String name){
        TrainingType trainingType = new TrainingType(name);

        trainingTypeDao.save(trainingType);
    }

    @Override
    public TrainingType findByName(String name) throws TrainingTypeNotFoundException {
        return trainingTypeDao.getByEntityByParam(NAME_PARAM,name).orElseThrow(
                ()-> new TrainingTypeNotFoundException(name)
        );
    }


}
