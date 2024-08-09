package org.tolking.dao.impl;

import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.tolking.dao.TraineeDAO;
import org.tolking.entity.Trainee;
import org.tolking.entity.Trainer;
import org.tolking.entity.Training;
import org.tolking.entity.User;
import org.tolking.enums.TrainingsType;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.tolking.dao.impl.TrainerDAOImpl.*;

@Repository
public class TraineeDAOImpl extends TraineeDAO {

    public static final String TRAINING_TYPE_ATTRIBUTE = "trainingType";

    public TraineeDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<Trainee> getByUsernameAndPassword(String username, String password) {
        return getByUsernameAndPassword(username,password, Trainee.class);
    }

    @Override
    public void delete(Trainee trainee){
        performSessionOperation(session -> session.remove(trainee));
    }

    @Override
    public List<Training> getTrainingsByCriteria(Date from, Date to, String trainerUsername, TrainingsType type, String traineeUsername) {
        try (Session session = this.sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Training> cr = cb.createQuery(Training.class);
            Root<Training> root = cr.from(Training.class);

            Predicate[] predicates = getPredicates(from, to, type, traineeUsername, trainerUsername, cb, root);

            cr.select(root).where(predicates);

            Query<Training> query = session.createQuery(cr);

            return query.getResultList();
        }
    }

    private static Predicate[] getPredicates(Date from, Date to,TrainingsType type, String traineeUsername, String trainerUsername, CriteriaBuilder cb, Root<Training> root) {
        Predicate[] predicates = new Predicate[4];
        predicates[0] = cb.equal(root.get(TRAINEE_ATTRIBUTE).get(USERS_TABLE_PARAM).get(USERNAME_ATTRIBUTE), traineeUsername);
        predicates[1] = cb.equal(root.get(TRAINER_ATTRIBUTE).get(USERS_TABLE_PARAM).get(USERNAME_ATTRIBUTE), trainerUsername);
        predicates[2] = cb.between(root.get(DATE_ATTRIBUTE), from, to);
        predicates[3] = cb.equal(root.get(TRAINING_TYPE_ATTRIBUTE).get("name"), type);
        return predicates;
    }

    @Override
    public List<Trainer> getNotAssignedTrainers(String traineeUsername) {
        try (Session session = this.sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Trainer> query = cb.createQuery(Trainer.class);
            Root<Trainer> trainerRoot = query.from(Trainer.class);

            // Subquery to find trainers assigned to the specific trainee
            Subquery<Trainer> subquery = query.subquery(Trainer.class);
            Root<Trainee> traineeRoot = subquery.from(Trainee.class);
            Join<Trainee, User> traineeUserJoin = traineeRoot.join(USERS_TABLE_PARAM);
            Join<Trainee, Trainer> traineeTrainerJoin = traineeRoot.join("trainerList");

            subquery.select(traineeTrainerJoin)
                    .where(cb.equal(traineeUserJoin.get(USERNAME_ATTRIBUTE), traineeUsername));

            //select trainers not in the list of assigned trainers
            query.select(trainerRoot)
                    .where(cb.not(trainerRoot.in(subquery)));


            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public Optional<Trainee> readByUserName(String username) {
        return readByUserName(username, Trainee.class);
    }

    @Override
    public void toggleStatus(Trainee trainee) {
        Boolean toggleStatus = !(trainee.getUser().getIsActive());
        trainee.getUser().setIsActive(toggleStatus);

        performSessionOperation(
                session -> session.merge(trainee)
        );

    }
}
