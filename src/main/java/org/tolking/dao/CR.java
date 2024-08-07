package org.tolking.dao;

import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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

    public void create(T entity) {
       this.performSessionOperation((session -> {
           session.persist(entity);
       }));
    }

    public  Optional<T> readByParam(String param, Object value, Class<T> entityClass) {
        try (Session session = this.sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> cr = cb.createQuery(entityClass);
            Root<T> root = cr.from(entityClass);

            cr.select(root).where(cb.equal(root.get(param), value));

            Query<T> query = session.createQuery(cr);

            return getSingleTypeOptional(query);
        }
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
