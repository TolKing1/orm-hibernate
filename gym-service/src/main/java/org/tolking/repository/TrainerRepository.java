package org.tolking.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tolking.entity.Trainer;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends CrudRepository<Trainer, Long> {
    Optional<Trainer> getTrainerByUser_UsernameAndUser_Password(String username, String password);

    Optional<Trainer> getTrainerByUser_Username(String username);

    @Query("SELECT tr FROM Trainer tr " +
            "WHERE tr.user.isActive = true AND " +
            "tr NOT IN (SELECT tr2 FROM Trainee te JOIN te.trainerList tr2 WHERE te.user.username = :username)")
    List<Trainer> findTrainersByUser_IsActiveTrueAndTraineeList_User_UsernameIsNot(@Param("username") String username);
}
