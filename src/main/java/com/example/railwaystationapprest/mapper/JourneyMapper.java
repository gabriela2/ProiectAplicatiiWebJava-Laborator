package com.example.railwaystationapprest.mapper;

import com.example.railwaystationapprest.dto.JourneyDto;
import com.example.railwaystationapprest.model.Company;
import com.example.railwaystationapprest.model.Journey;
import com.example.railwaystationapprest.model.RailwayStation;
import com.example.railwaystationapprest.model.Train;
import com.example.railwaystationapprest.service.CompanyService;
import com.example.railwaystationapprest.service.RailwayStationService;
import com.example.railwaystationapprest.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JourneyMapper {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private TrainService trainService;
    @Autowired
    private RailwayStationService railwayStationService;

    public JourneyDto convertJourneyToJourneyDto(Journey journey){
        return new JourneyDto(journey.getDistance(),journey.getDepartureTime(),journey.getArrivalTime(),journey.getMinuteLate(),journey.getTicketPrice(),journey.getJourneyStatus(),journey.getTrain().getCode(),journey.getCompany().getName(),journey.getDepartureRailwayStation().getName(),journey.getArrivalRailwayStation().getName());
    }

    public Journey convertJourneyDtoToJourney(JourneyDto journeyDto){
        Company company = companyService.getCompanyByName(journeyDto.getCompanyName());
        Train train = trainService.getTrainByCode(journeyDto.getTrainCode());
        RailwayStation departure = railwayStationService.getRailwayStationByName(journeyDto.getDepartureRailway());
        RailwayStation arrival = railwayStationService.getRailwayStationByName(journeyDto.getArrivalRailway());
        return new Journey(journeyDto.getDistance(),journeyDto.getDepartureTime(),journeyDto.getArrivalTime(),journeyDto.getMinuteLate(),journeyDto.getTicketPrice(),journeyDto.getJourneyStatus(),train,departure,arrival,company);

    }
}
