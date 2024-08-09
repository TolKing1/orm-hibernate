package org.tolking.dao;

import org.hibernate.SessionFactory;
import org.tolking.entity.Trainee;
import org.tolking.entity.Trainer;
import org.tolking.entity.Training;
import org.tolking.enums.TrainingsType;

import java.util.Date;
import java.util.List;

public abstract class TraineeDAO extends AbstractUserDAO<Trainee>{

    public TraineeDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public abstract void delete(Trainee trainee);

    public abstract List<Training> getTrainingsByCriteria(Date from, Date to, String trainerUsername, TrainingsType type, String traineeUsername);

    public abstract List<Trainer> getNotAssignedTrainers(String traineeUsername);
}
