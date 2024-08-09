package org.tolking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.tolking.dao.TrainerDAO;
import org.tolking.dto.CriteriaTrainerDTO;
import org.tolking.dto.LoginDTO;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.converter.trainer.TrainerUpdateConverter;
import org.tolking.dto.trainer.TrainerDTO;
import org.tolking.dto.trainer.TrainerProfileDTO;
import org.tolking.dto.trainer.TrainerUpdateDTO;
import org.tolking.dto.training.TrainingReadDTO;
import org.tolking.entity.Trainer;
import org.tolking.entity.Training;
import org.tolking.entity.User;
import org.tolking.exception.UserNotFoundException;
import org.tolking.util.UserUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log
public class TrainerServiceImpl implements org.tolking.service.TrainerService {
    private final TrainerDAO trainerDAO;
    private final DTOConverter<Trainer, TrainerDTO> createConverter;
    private final DTOConverter<Trainer, TrainerProfileDTO> profileConverter;
    private final TrainerUpdateConverter updateConverter;
    private final DTOConverter<Training, TrainingReadDTO> trainingConverter;

    @Override
    public void create(TrainerDTO dto){
        Trainer trainer = createConverter.toEntity(dto);
        User trainerUser = trainer.getUser();
        String serialUsername = trainerDAO.getUsername(trainerUser.getUsername());

        UserUtils.prepareUserForCreation(trainerUser,serialUsername);

        trainerDAO.create(trainer);
        log.info("Trainer has been created with username = %s".formatted(serialUsername));
    }

    @Override
    public TrainerProfileDTO getProfile(LoginDTO dto) throws UserNotFoundException {
        Trainer trainer = getTrainer(dto);

        return profileConverter.toDto(trainer);
    }

    @Override
    public void updatePassword(LoginDTO dto, String newPassword) throws UserNotFoundException {
        Trainer trainer = getTrainer(dto);

        trainer.getUser().setPassword(newPassword);

        trainerDAO.update(trainer);
        log.info("Trainer's password has been updated with id = %s".formatted(trainer.getId()));
    }



    @Override
    public void update(LoginDTO loginDTO, TrainerUpdateDTO trainerUpdateDTO) throws UserNotFoundException {
        Trainer trainer = getTrainer(loginDTO);

        trainer = updateConverter.updateEntity(trainer, trainerUpdateDTO);

        trainerDAO.update(trainer);
        log.info("Trainer has been updated with id = %s".formatted(trainer.getId()));
    }

    @Override
    public void toggleStatus(LoginDTO loginDTO) throws UserNotFoundException {
        Trainer trainer = getTrainer(loginDTO);

        trainerDAO.toggleStatus(trainer);
        log.info("Trainer's status has been toggled with id = %s".formatted(trainer.getId()));
    }

    @Override
    public List<TrainingReadDTO> getTrainingList(LoginDTO loginDTO, CriteriaTrainerDTO criteria) {
        Trainer trainer = getTrainer(loginDTO);
        String username = trainer.getUser().getUsername();
        List<Training> trainingList = trainerDAO.getTrainingsByCriteria(
                criteria.getFrom(),
                criteria.getTo(),
                criteria.getTraineeUsername(),
                username
        );

        return trainingConverter.toDtoList(trainingList);
    }

    private Trainer getTrainer(LoginDTO dto) throws UserNotFoundException {
        return trainerDAO.getByUsernameAndPassword(dto.getUsername(), dto.getPassword())
                .orElseThrow(()-> {
                    log.warning("Login credentials is incorrect");
                    return new UserNotFoundException(dto.getUsername());
                });
    }
}
