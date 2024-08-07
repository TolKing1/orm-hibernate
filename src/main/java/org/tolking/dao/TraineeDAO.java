package org.tolking.dao;

import org.hibernate.SessionFactory;
import org.tolking.entity.Trainee;

public abstract class TraineeDAO extends AbstractUserDAO<Trainee>{

    public TraineeDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public abstract void delete(Trainee trainee);
}
