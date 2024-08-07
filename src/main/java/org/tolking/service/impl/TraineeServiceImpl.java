package org.tolking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.tolking.dao.TraineeDAO;
import org.tolking.dto.LoginDTO;
import org.tolking.dto.converter.DTOConverter;
import org.tolking.dto.converter.trainee.TraineeProfileConverter;
import org.tolking.dto.trainee.TraineeDTO;
import org.tolking.dto.trainee.TraineeProfileDTO;
import org.tolking.entity.Trainee;
import org.tolking.exception.UserNotFoundException;
import org.tolking.service.TraineeService;

@Service
@RequiredArgsConstructor
@Log
public class TraineeServiceImpl implements TraineeService {
    private final TraineeDAO traineeDAO;
    private final DTOConverter<Trainee, TraineeDTO> createConverter;
    private final TraineeProfileConverter profileConverter;

    @Override
    public void create(TraineeDTO dto) {
        Trainee trainee = createConverter.toEntity(dto);
        trainee.getUser().setIsActive(true);

        traineeDAO.create(trainee);
    }

    @Override
    public TraineeProfileDTO getProfile(LoginDTO dto) throws UserNotFoundException {
        Trainee trainee = getTrainee(dto);

        return profileConverter.toDto(trainee);
    }

    @Override
    public void updatePassword(LoginDTO dto, String newPassword) throws UserNotFoundException {
        Trainee trainee = getTrainee(dto);

        trainee.getUser().setPassword(newPassword);

        traineeDAO.update(trainee);
    }

    @Override
    public void update(LoginDTO loginDTO, TraineeProfileDTO trainerUpdateDTO) throws UserNotFoundException {
        Trainee trainee = getTrainee(loginDTO);

        trainee = profileConverter.updateEntity(trainee, trainerUpdateDTO);

        traineeDAO.update(trainee);
    }

    @Override
    public void toggleStatus(LoginDTO loginDTO) throws UserNotFoundException {
        Trainee trainee = getTrainee(loginDTO);

        traineeDAO.toggleStatus(trainee);
    }

    @Override
    public void delete(LoginDTO loginDTO) throws UserNotFoundException {
        Trainee trainee = getTrainee(loginDTO);

        traineeDAO.delete(trainee);
    }

    private Trainee getTrainee(LoginDTO dto) throws UserNotFoundException {
        return traineeDAO.getByUsernameAndPassword(dto.getUsername(), dto.getPassword())
                .orElseThrow(()-> new UserNotFoundException(dto.getUsername()));
    }
}
