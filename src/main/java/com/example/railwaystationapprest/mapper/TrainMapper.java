package com.example.railwaystationapprest.mapper;

import com.example.railwaystationapprest.dto.TrainDto;
import com.example.railwaystationapprest.model.Train;
import org.springframework.stereotype.Component;

@Component
public class TrainMapper {
    public Train convertTrainDtoToTrain(TrainDto trainDto){
        return new Train(trainDto.getCode(),trainDto.getNumberOfSeats(),trainDto.getFuelType(),trainDto.getTrainType());
    }

    public TrainDto convertTrainToTrainDto(Train train){
        return new TrainDto(train.getCode(),train.getNumberOfSeats(),train.getFuelType(),train.getTrainType());
    }
}
