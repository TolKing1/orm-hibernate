package org.tolking.dao;

import org.hibernate.SessionFactory;
import org.tolking.entity.Trainer;
import org.tolking.entity.Training;

import java.util.Date;
import java.util.List;

public abstract class TrainerDAO extends AbstractUserDAO<Trainer>{
    public TrainerDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public abstract List<Training> getTrainingsByCriteria(Date from, Date to, String traineeUsername, String trainerUsername);

}
