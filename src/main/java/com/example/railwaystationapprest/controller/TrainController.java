package com.example.railwaystationapprest.controller;

import com.example.railwaystationapprest.dto.TrainDto;
import com.example.railwaystationapprest.exception.InvalidUpdateException;
import com.example.railwaystationapprest.mapper.TrainMapper;
import com.example.railwaystationapprest.model.Train;
import com.example.railwaystationapprest.service.Credentials;
import com.example.railwaystationapprest.service.TrainService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/train")
@Validated
public class TrainController {
    private TrainService trainService;
    private TrainMapper trainMapper;

    public TrainController(TrainService trainService, TrainMapper trainMapper) {
        this.trainService = trainService;
        this.trainMapper = trainMapper;
    }

    @GetMapping
    public ResponseEntity<List<TrainDto>> getTrains(@RequestHeader(Credentials.AUTHORIZATION) String header){
        Credentials credentials = new Credentials(header);
        List<Train> trains = trainService.getTrains(credentials);
        return trains.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(trains.stream().map(train->trainMapper.convertTrainToTrainDto(train)).collect(Collectors.toList()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<TrainDto> getTrainById(@RequestHeader(Credentials.AUTHORIZATION) String header,@PathVariable long id){
        Credentials credentials = new Credentials(header);
        Train train = trainService.getTrainById(credentials, id);
        return ResponseEntity.ok(trainMapper.convertTrainToTrainDto(train));
    }

    @PostMapping
    public ResponseEntity<Train> createTrain(@RequestHeader(Credentials.AUTHORIZATION) String header, @Valid @RequestBody TrainDto trainDto){
        Credentials credentials = new Credentials(header);
        Train savedTrain = trainService.create(trainMapper.convertTrainDtoToTrain(trainDto),credentials);
        return ResponseEntity.created(URI.create("/train/"+savedTrain.getId())).body(savedTrain);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainDto> updateTrain(@RequestHeader(Credentials.AUTHORIZATION) String header, @Valid @RequestBody Train train, @PathVariable long id) {
        if (id != train.getId()) {
            throw new InvalidUpdateException();
        }
        Credentials credentials = new Credentials(header);
        return ResponseEntity.ok(trainMapper.convertTrainToTrainDto(trainService.updateTrain(train, credentials)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(@RequestHeader(Credentials.AUTHORIZATION) String header, @PathVariable long id){
        Credentials credentials = new Credentials(header);
        trainService.deleteTrain(id, credentials);
        return ResponseEntity.noContent().build();
    }


}
