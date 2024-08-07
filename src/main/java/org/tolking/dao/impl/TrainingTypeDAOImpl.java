package org.tolking.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.tolking.dao.CR;
import org.tolking.entity.TrainingType;

@Repository
public class TrainingTypeDAOImpl extends CR<TrainingType> {

    public TrainingTypeDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


}
