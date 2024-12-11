package org.tolking.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.tolking.entity.Training;
import org.tolking.enums.TrainingsType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainingRepository extends CrudRepository<Training, Long> {
    /**
     * Finds a list of training sessions based on the specified criteria and training that is not deleted.
     *
     * @param username    the username of the trainer whose trainings are to be fetched (required).
     * @param periodFrom  the start date from which to filter trainings (optional). If null, no lower bound is applied.
     * @param periodTo    the end date until which to filter trainings (optional). If null, no upper bound is applied.
     * @param traineeName the username of a trainee to filter trainings by (optional). If null, no trainee filtering is applied.
     * @return a list of {@link Training} entities that match the specified criteria. If no trainings match the criteria, an empty list is returned.
     * @throws IllegalArgumentException if the username is null or empty.
     */
    @Query("SELECT t FROM Training t " +
            "WHERE t.trainer.user.username = :username " +
            "AND ( t.isDeleted = false ) " +
            "AND ( CAST(CAST(:periodFrom as string ) as timestamp) IS NULL OR t.date >= :periodFrom ) " +
            "AND ( CAST(CAST(:periodTo as string ) as timestamp) IS NULL OR t.date <= :periodTo ) " +
            "AND ( :traineeName IS NULL OR t.trainee.user.username = :traineeName)")
    List<Training> findTrainerTrainingsByCriteria(@Param("username") String username,
                                                  @Param("periodFrom") LocalDate periodFrom,
                                                  @Param("periodTo") LocalDate periodTo,
                                                  @Param("traineeName") String traineeName);


    /**
     * Finds a list of trainings for a specific trainee based on various criteria and training that is not deleted.
     *
     * @param username      The username of the trainee. Cannot be null.
     * @param periodFrom    The start date of the period to filter trainings. Can be null to include all dates before.
     * @param periodTo      The end date of the period to filter trainings. Can be null to include all dates after.
     * @param trainerName   The username of the trainer. Can be null to include all trainers.
     * @param trainingsType The type of training. Can be null to include all training types.
     * @return A list of {@link Training} entities that match the specified criteria.
     */
    @Query("SELECT t FROM Training t " +
            "WHERE t.trainee.user.username = :username " +
            "AND ( t.isDeleted = false ) " +
            "AND (CAST(CAST(:periodFrom as string ) as timestamp) IS NULL OR t.date >= :periodFrom) " +
            "AND (CAST(CAST(:periodTo as string ) as timestamp) IS NULL OR t.date <= :periodTo) " +
            "AND (:trainerName IS NULL OR t.trainer.user.username = :trainerName)" +
            "AND (:trainingsType IS NULL OR t.trainingType.name = :trainingsType)")
    List<Training> findTraineeTrainingsByCriteria(@Param("username") String username,
                                                  @Param("periodFrom") LocalDate periodFrom,
                                                  @Param("periodTo") LocalDate periodTo,
                                                  @Param("trainerName") String trainerName,
                                                  @Param("trainingsType") TrainingsType trainingsType);

    Optional<Training> findTrainingByTraineeUserUsernameAndId(String traineeUserUsername, long id);

}
