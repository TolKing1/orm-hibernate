package org.tolking.dao;

import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Optional;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Log
public abstract class CR<T> implements QueryHelper<T> {
    protected final SessionFactory sessionFactory;

    public abstract Optional<T> getByEntityByParam(String param, String value);

    public void save(T entity) {
       this.performSessionOperation((session -> {
           session.persist(entity);
       }));
    }

    @Override
    public void performSessionOperation(Consumer<Session> sessionConsumer) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            sessionConsumer.accept(session);

            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            log.warning("Transaction can't be performed: %s".formatted(ex.getMessage()));
        }
    }

    @Override
    public Optional<T> getSingleTypeOptional(Query<T> query) {
        try {
            return Optional.of(query.getSingleResult());
        }
        catch (NonUniqueResultException e){
            return Optional.of(query.getResultList().get(0));
        }
        catch (NoResultException e){
            return Optional.empty();
        }
    }
}
