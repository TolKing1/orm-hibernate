package org.tolking.dao;

import org.hibernate.SessionFactory;
import org.tolking.entity.Trainer;

public abstract class TrainerDAO extends AbstractUserDAO<Trainer>{
    public TrainerDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
