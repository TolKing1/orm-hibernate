package org.tolking.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.tolking.dao.TraineeDAO;
import org.tolking.entity.Trainee;

import java.util.Optional;

@Repository
public class TraineeDAOImpl extends TraineeDAO {
    public TraineeDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<Trainee> getByUsernameAndPassword(String username, String password) {
        return getByUsernameAndPassword(username,password, Trainee.class);
    }

    @Override
    public void delete(Trainee trainee){
        performSessionOperation(session -> {
            session.remove(trainee);
        });
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
                session -> {
                    session.merge(trainee);
                }
        );

    }
}
