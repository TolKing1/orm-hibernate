package org.tolking.dao.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.tolking.dao.TrainerDAO;
import org.tolking.entity.Trainer;
import org.tolking.entity.Training;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainerDAOImpl extends TrainerDAO {

    public static final String TRAINEE_ATTRIBUTE = "trainee";
    public static final String TRAINER_ATTRIBUTE = "trainer";
    public static final String DATE_ATTRIBUTE = "date";

    public TrainerDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Training> getTrainingsByCriteria(Date from, Date to, String traineeUsername, String trainerUsername) {
        try (Session session = this.sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Training> cr = cb.createQuery(Training.class);
            Root<Training> root = cr.from(Training.class);

            Predicate[] predicates = getPredicates(from, to, traineeUsername, trainerUsername, cb, root);

            cr.select(root).where(predicates);

            Query<Training> query = session.createQuery(cr);

            return query.getResultList();
        }
    }

    private static Predicate[] getPredicates(Date from, Date to, String traineeUsername, String trainerUsername, CriteriaBuilder cb, Root<Training> root) {
        Predicate[] predicates = new Predicate[3];
        predicates[0] = cb.equal(root.get(TRAINEE_ATTRIBUTE).get(USERS_TABLE_PARAM).get(USERNAME_ATTRIBUTE), traineeUsername);
        predicates[1] = cb.equal(root.get(TRAINER_ATTRIBUTE).get(USERS_TABLE_PARAM).get(USERNAME_ATTRIBUTE), trainerUsername);
        predicates[2] = cb.between(root.get(DATE_ATTRIBUTE), from, to);
        return predicates;
    }

    @Override
    public Optional<Trainer> getByUsernameAndPassword(String username, String password) {
        return getByUsernameAndPassword(username, password, Trainer.class);
    }

    @Override
    public Optional<Trainer> readByUserName(String username) {
        return readByUserName(username, Trainer.class);
    }

    @Override
    public void toggleStatus(Trainer trainer) {
        Boolean toggleStatus = !(trainer.getUser().getIsActive());
        trainer.getUser().setIsActive(toggleStatus);

        performSessionOperation(
                session -> session.merge(trainer)
        );
    }
}
