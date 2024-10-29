package org.tolking.service.impl;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.tolking.controller.client.TrainingEventClient;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.criteria.CriteriaTraineeDTO;
import org.tolking.dto.criteria.CriteriaTrainerDTO;
import org.tolking.dto.training.TrainingDTO;
import org.tolking.dto.training.TrainingTraineeReadDTO;
import org.tolking.dto.training.TrainingTrainerReadDTO;
import org.tolking.entity.Trainee;
import org.tolking.entity.Trainer;
import org.tolking.entity.Training;
import org.tolking.enums.ActionType;
import org.tolking.exception.TraineeNotFoundException;
import org.tolking.exception.TrainerNotFoundException;
import org.tolking.exception.TrainingNotFoundException;
import org.tolking.external_dto.TrainingEventDTO;
import org.tolking.repository.TrainingRepository;
import org.tolking.service.TrainerService;
import org.tolking.service.TrainingService;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;

    private final TrainerService trainerService;
    private final TrainingEventClient trainingEventClient;

    private final DTOConverter<Training, TrainingTraineeReadDTO> readTraineeConverter;
    private final DTOConverter<Training, TrainingTrainerReadDTO> readTrainerConverter;
    private final ModelMapper modelMapper;

    public TrainingServiceImpl(TrainingRepository trainingRepository,
                               @Lazy TrainerService trainerService,
                               DTOConverter<Training, TrainingTraineeReadDTO> readTraineeConverter,
                               DTOConverter<Training, TrainingTrainerReadDTO> readTrainerConverter,
                               ModelMapper modelMapper,
                               TrainingEventClient trainingEventClient) {
        this.trainingRepository = trainingRepository;
        this.trainerService = trainerService;
        this.readTraineeConverter = readTraineeConverter;
        this.readTrainerConverter = readTrainerConverter;
        this.modelMapper = modelMapper;
        this.trainingEventClient = trainingEventClient;
    }

    @Override
    public List<TrainingTraineeReadDTO> getTraineeTrainingListByCriteria(String username, CriteriaTraineeDTO criteria) {
        log.debug("Fetching trainee training list for username: {} with criteria: {}", username, criteria);

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
        log.debug("Fetching trainer training list for username: {} with criteria: {}", username, criteria);

        List<Training> trainingList = trainingRepository.findTrainerTrainingsByCriteria(
                username,
                criteria.getFrom(),
                criteria.getTo(),
                criteria.getTraineeUsername()
        );
        return readTrainerConverter.toDtoList(trainingList);
    }

    @Override
    public TrainingEventDTO createTraining(Trainee trainee, TrainingDTO dto) throws TrainerNotFoundException, TraineeNotFoundException {
        log.debug("Creating training with DTO: {} for trainee: {}", dto, trainee.getUser().getUsername());

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

        Training createdTraining = trainingRepository.save(training);

        log.debug("Training created successfully with ID: {}", createdTraining.getId());

        TrainingEventDTO trainingEventDTO = modelMapper.map(createdTraining, TrainingEventDTO.class);

        //Create event
        createTrackerEvent(trainingEventDTO, ActionType.ADD);

        return trainingEventDTO;
    }

    @Override
    public TrainingEventDTO cancelTraining(String traineeUsername, long id) throws TrainingNotFoundException{
        log.debug("Cancelling training with id: {}", id);

        Training training = trainingRepository.findTrainingByTraineeUserUsernameAndId(traineeUsername, id)
                .orElseThrow(()-> new TrainingNotFoundException(id));

        training.setDeleted(true);
        training.setDeleteDate(new Date());

        Training updatedTraining = trainingRepository.save(training);

        log.debug("Training cancelled successfully with ID: {}", id);

        TrainingEventDTO trainingEventDTO = modelMapper.map(updatedTraining, TrainingEventDTO.class);

        //Create event
        createTrackerEvent(trainingEventDTO, ActionType.DELETE);

        return trainingEventDTO;
    }

    private void createTrackerEvent(TrainingEventDTO createdTraining, ActionType actionType) {
        try {
            createdTraining.setActionType(actionType);

            trainingEventClient.createEvent(createdTraining);
        } catch (FeignException.FeignClientException e) {
            log.warn("Tracking service is not available");
        }
    }
}
