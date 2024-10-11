package org.tolking.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.criteria.CriteriaTraineeDTO;
import org.tolking.dto.criteria.CriteriaTrainerDTO;
import org.tolking.dto.training.TrainingDTO;
import org.tolking.dto.training.TrainingTraineeReadDTO;
import org.tolking.dto.training.TrainingTrainerReadDTO;
import org.tolking.entity.Trainee;
import org.tolking.entity.Trainer;
import org.tolking.entity.Training;
import org.tolking.exception.TraineeNotFoundException;
import org.tolking.exception.TrainerNotFoundException;
import org.tolking.repository.TrainingRepository;
import org.tolking.service.TrainerService;

import java.util.List;

@Service
@Slf4j
public class TrainingServiceImpl implements org.tolking.service.TrainingService {
    private final TrainingRepository trainingRepository;

    private final TrainerService trainerService;

    private final DTOConverter<Training, TrainingTraineeReadDTO> readTraineeConverter;
    private final DTOConverter<Training, TrainingTrainerReadDTO> readTrainerConverter;

    public TrainingServiceImpl(TrainingRepository trainingRepository, @Lazy TrainerService trainerService, DTOConverter<Training, TrainingTraineeReadDTO> readTraineeConverter, DTOConverter<Training, TrainingTrainerReadDTO> readTrainerConverter) {
        this.trainingRepository = trainingRepository;
        this.trainerService = trainerService;
        this.readTraineeConverter = readTraineeConverter;
        this.readTrainerConverter = readTrainerConverter;
    }

    @Override
    public List<TrainingTraineeReadDTO> getTraineeTrainingListByCriteria(String username, CriteriaTraineeDTO criteria) {
        log.info("Fetching trainee training list for username: {} with criteria: {}", username, criteria);

        List<Training> trainingList = trainingRepository.findTraineeTrainingsByCriteria(
                username,
                criteria.getFrom(),
                criteria.getTo(),
                criteria.getTrainerUsername(),
                criteria.getTrainingType()
        );
        return readTraineeConverter.toDtoList(trainingList);
    }

    @Override
    public List<TrainingTrainerReadDTO> getTrainerTrainingListByCriteria(String username, CriteriaTrainerDTO criteria) {
        log.info("Fetching trainer training list for username: {} with criteria: {}", username, criteria);

        List<Training> trainingList = trainingRepository.findTrainerTrainingsByCriteria(
                username,
                criteria.getFrom(),
                criteria.getTo(),
                criteria.getTraineeUsername()
        );
        return readTrainerConverter.toDtoList(trainingList);
    }

    @Override
    public void createTraining(Trainee trainee, TrainingDTO dto) throws TrainerNotFoundException, TraineeNotFoundException {
        log.info("Creating training with DTO: {} for trainee: {}", dto, trainee.getUser().getUsername());

        Trainer trainer = trainerService.getTrainerByUsername(dto.getTrainerUsername());
        if (trainer == null) {
            throw new TrainerNotFoundException(dto.getTrainerUsername());
        }

        Training training = Training.builder()
                .trainer(trainer)
                .trainee(trainee)
                .trainingName(dto.getTrainingName())
                .date(dto.getDate())
                .duration(dto.getDuration())
                .trainingType(trainer.getTrainingType())
                .build();

        Long id = trainingRepository.save(training).getId();

        log.info("Training created successfully with ID: {}", id);
    }
}
