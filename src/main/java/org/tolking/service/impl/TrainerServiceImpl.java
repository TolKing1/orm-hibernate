package org.tolking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.tolking.dao.TrainerDAO;
import org.tolking.dto.LoginDTO;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.converter.trainer.TrainerUpdateConverter;
import org.tolking.dto.trainer.TrainerDTO;
import org.tolking.dto.trainer.TrainerProfileDTO;
import org.tolking.dto.trainer.TrainerUpdateDTO;
import org.tolking.entity.Trainer;
import org.tolking.exception.UserNotFoundException;

@Service
@RequiredArgsConstructor
@Log
public class TrainerServiceImpl implements org.tolking.service.TrainerService {
    private final TrainerDAO trainerDAO;
    private final DTOConverter<Trainer, TrainerDTO> createConverter;
    private final DTOConverter<Trainer, TrainerProfileDTO> profileConverter;
    private final TrainerUpdateConverter updateConverter;

    @Override
    public void create(TrainerDTO dto){
        Trainer trainer = createConverter.toEntity(dto);
        trainer.getUser().setIsActive(true);

        trainerDAO.create(trainer);
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
    }



    @Override
    public void update(LoginDTO loginDTO, TrainerUpdateDTO trainerUpdateDTO) throws UserNotFoundException {
        Trainer trainer = getTrainer(loginDTO);

        trainer = updateConverter.updateEntity(trainer, trainerUpdateDTO);

        trainerDAO.update(trainer);
    }

    @Override
    public void toggleStatus(LoginDTO loginDTO) throws UserNotFoundException {
        Trainer trainer = getTrainer(loginDTO);

        trainerDAO.toggleStatus(trainer);
    }

    private Trainer getTrainer(LoginDTO dto) throws UserNotFoundException {
        return trainerDAO.getByUsernameAndPassword(dto.getUsername(), dto.getPassword())
                .orElseThrow(()-> new UserNotFoundException(dto.getUsername()));
    }
}
