package org.tolking.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.tolking.dao.TrainingTypeDAO;
import org.tolking.entity.TrainingType;
import org.tolking.enums.TrainingsType;

import java.util.Optional;

@Repository
public class TrainingTypeDAOImpl extends TrainingTypeDAO {

    public static final String NAME_PARAM = "name";

    public TrainingTypeDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<TrainingType> readByName(TrainingsType name){
        return readByParam(NAME_PARAM,name, TrainingType.class);
    }


}
