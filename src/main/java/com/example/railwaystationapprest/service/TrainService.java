package com.example.railwaystationapprest.service;

import com.example.railwaystationapprest.exception.*;
import com.example.railwaystationapprest.model.Train;
import com.example.railwaystationapprest.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainService {
    private TrainRepository trainRepository;
    private JourneyService journeyService;

    public TrainService(TrainRepository trainRepository, JourneyService journeyService) {
        this.trainRepository = trainRepository;
        this.journeyService = journeyService;
    }

    @Value("${administrator.email}")
    private String adminEmail;
    @Value("${administrator.password}")
    private String adminPassword;

    private void checkCredentials(Credentials credentials) {
        if(!(credentials.getEmail().equals(adminEmail) && credentials.getPassword().equals(adminPassword))){
            throw new BadCredentialsException();
        }
    }

    public List<Train> getTrains(Credentials credentials) {
        checkCredentials(credentials);
        return trainRepository.findAll();
    }

    public Train getTrainById(Credentials credentials, long id) {
        checkCredentials(credentials);
        return trainRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Train"));
    }

    public Train create(Train train, Credentials credentials) {
        checkCredentials(credentials);
        Optional<Train> existingTrain = trainRepository.findByCode(train.getCode());
        if(existingTrain.isPresent()){
            throw new ObjectAlreadyExistsException("Train");
        }
        return trainRepository.save(train);
    }

    public Train updateTrain(Train train, Credentials credentials) {
        checkCredentials(credentials);
        Train existingTrain = trainRepository.findById(train.getId()).orElseThrow(()-> new ObjectNotFoundException("Train"));
        if(!existingTrain.getCode().equals(train.getCode())){
            throw new ObjectCannotBeUpdatedException("code");
        }
        return trainRepository.save(train);

    }

    public void deleteTrain(long id, Credentials credentials) {
        checkCredentials(credentials);
        trainRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Train"));
        if(journeyService.getJourneysByTrainId(id).isEmpty()){
            trainRepository.deleteById(id);
        }else {
            throw new ObjectCannotBeDeletedException("Train");
        }
    }

    public Train getTrainByCode(String trainCode) {
        Train existingTrain = trainRepository.findByCode(trainCode).orElseThrow(()-> new ObjectNotFoundException("Train"));
        return existingTrain;
    }
}
