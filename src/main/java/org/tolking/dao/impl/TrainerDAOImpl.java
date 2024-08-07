package org.tolking.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.tolking.dao.TrainerDAO;
import org.tolking.entity.Trainer;

import java.util.Optional;

@Repository
public class TrainerDAOImpl extends TrainerDAO {

    public TrainerDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
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
                session -> {
                    session.merge(trainer);
                }
        );
    }
}
