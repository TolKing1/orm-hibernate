package org.tolking.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Optional;
import java.util.function.Consumer;

public interface QueryHelper<T> {
    Optional<T> getSingleTypeOptional(Query<T> query);
    void performSessionOperation(Consumer<Session> sessionConsumer);
}
