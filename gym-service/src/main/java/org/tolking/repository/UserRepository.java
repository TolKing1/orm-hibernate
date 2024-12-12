package org.tolking.repository;

import org.springframework.data.repository.CrudRepository;
import org.tolking.entity.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> getUserByUsername(String username);

    Optional<User> findByUsernameAndIsActiveTrue(String username);
}
