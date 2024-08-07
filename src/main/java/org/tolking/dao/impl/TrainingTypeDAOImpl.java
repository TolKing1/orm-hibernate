package org.tolking.dao.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.tolking.dao.CR;
import org.tolking.entity.TrainingType;
import org.tolking.enums.TrainingsType;

import java.util.Optional;

@Repository
public class TrainingTypeDAOImpl extends CR<TrainingType> {

    public TrainingTypeDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<TrainingType> getByEntityByParam(String param, String value) {
        try(Session session = this.sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<TrainingType> cr = cb.createQuery(TrainingType.class);
            Root<TrainingType> root = cr.from(TrainingType.class);

            cr.select(root).where(cb.equal(root.get(param), TrainingsType.valueOf(value)));

            Query<TrainingType> query = session.createQuery(cr);

            return getSingleTypeOptional(query);
        }
    }

}
