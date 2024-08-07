package org.tolking.dao;

import org.hibernate.SessionFactory;
import org.tolking.entity.TrainingType;
import org.tolking.enums.TrainingsType;

import java.util.Optional;

public abstract class TrainingTypeDAO extends AbstractDAO<TrainingType> {
    public TrainingTypeDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public abstract Optional<TrainingType> readByName(TrainingsType name);

}
