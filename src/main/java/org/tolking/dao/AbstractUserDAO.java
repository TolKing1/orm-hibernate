package org.tolking.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Optional;


public abstract class AbstractUserDAO<T> extends AbstractDAO<T> {

    public static final String USERS_TABLE_PARAM = "user";
    public static final String USERNAME_ATTRIBUTE = "username";
    public static final String PASSWORD_ATTRIBUTE = "password";

    public AbstractUserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void update(T entity) {
        this.performSessionOperation((session -> {
            session.merge(entity);
        }));
    }
    public String getUsername(String baseUsername){
        int serialNumber = 1;
        String username = baseUsername;

        while (this.readByUserName(username).isPresent()) {
            username = baseUsername + "_" + serialNumber++;
        }

        return username;
    }
    public abstract Optional<T> getByUsernameAndPassword(String username, String password);

    public abstract Optional<T> readByUserName(String username);

    public abstract void toggleStatus(T entity);

    protected Optional<T> readByUserName(String username, Class<T> entityClass) {
        try (Session session = this.sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> cr = cb.createQuery(entityClass);
            Root<T> root = cr.from(entityClass);

            cr.select(root).where(cb.equal(root.get(USERS_TABLE_PARAM).get(USERNAME_ATTRIBUTE), username));

            Query<T> query = session.createQuery(cr);

            return getSingleTypeOptional(query);
        }
    }

    protected Optional<T> getByUsernameAndPassword(String username, String password, Class<T> entityClass){
        try (Session session = this.sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> cr = cb.createQuery(entityClass);
            Root<T> root = cr.from(entityClass);

            // Create predicates for the conditions
            Predicate usernamePredicate = cb.equal(root.get(USERS_TABLE_PARAM).get(USERNAME_ATTRIBUTE), username);
            Predicate passwordPredicate = cb.equal(root.get(USERS_TABLE_PARAM).get(PASSWORD_ATTRIBUTE), password);

            // Combine predicates with "and"
            cr.select(root).where(cb.and(usernamePredicate, passwordPredicate));;

            Query<T> query = session.createQuery(cr);

            return getSingleTypeOptional(query);
        }
    }

}
