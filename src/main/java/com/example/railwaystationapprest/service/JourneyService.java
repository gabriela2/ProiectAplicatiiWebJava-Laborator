package com.example.railwaystationapprest.service;

import com.example.railwaystationapprest.exception.*;
import com.example.railwaystationapprest.model.Journey;
import com.example.railwaystationapprest.model.JourneyStatus;
import com.example.railwaystationapprest.repository.JourneyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class JourneyService {
    private JourneyRepository journeyRepository;

    public JourneyService(JourneyRepository journeyRepository) {
        this.journeyRepository = journeyRepository;
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

    public List<Journey> getJourneysByTrainId(long id) {
        return journeyRepository.findAllByTrainId(id);

    }

    public Journey createJourney(Journey journey, Credentials credentials) {
        checkCredentials(credentials);
        Optional<Journey> existingJourney = journeyRepository.findJourney(journey.getCompany().getId(),journey.getDepartureTime(),journey.getArrivalTime(),journey.getTrain().getId(),journey.getDepartureRailwayStation().getId(),journey.getArrivalRailwayStation().getId());
        if (!existingJourney.isEmpty()) {
            throw new ObjectAlreadyExistsException("Journey");
        } else {
            if (journey.getDepartureRailwayStation() == journey.getArrivalRailwayStation()) {
                throw new ObjectCannotBeCreatedException("Journey");
            }
            if (journey.getDepartureTime().compareTo(journey.getArrivalTime()) > 0 || new Timestamp(System.currentTimeMillis()).compareTo(journey.getDepartureTime()) > 0) {
                throw new ObjectCannotBeCreatedException("Journey");
            }
            return journeyRepository.save(journey);
        }
    }

    public List<Journey> getJourneysByCompanyId(long id) {
        return journeyRepository.findAllByCompanyId(id);
    }

    public Journey findJourneyById(long id){
        Journey journey = journeyRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("journey"));
        return journey;
    }

    public void deleteJourney(long id, Credentials credentials) {
        checkCredentials(credentials);
        Journey exitingJourney = journeyRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Journey"));
        if (exitingJourney.getTickets().size() == 0 && exitingJourney.getDepartureTime().compareTo(new Timestamp(System.currentTimeMillis())) > 0) {
            journeyRepository.delete(exitingJourney);
        } else {
            throw new ObjectCannotBeDeletedException("Journey");
        }
    }

    public Journey updateJourney(Long id, Journey journey,Credentials credentials) {
        checkCredentials(credentials);
        Journey existingJourney = journeyRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Journey"));
        if (existingJourney.getArrivalTime().compareTo(new Timestamp(System.currentTimeMillis())) > 0) {
            existingJourney.setMinuteLate(journey.getMinuteLate());
            existingJourney.setDistance(journey.getDistance());
            existingJourney.setTrain(journey.getTrain());
            existingJourney.setCompany(journey.getCompany());
            existingJourney.setTicketPrice(journey.getTicketPrice());
            existingJourney.setJourneyStatus(journey.getJourneyStatus());
            if (journey.getDepartureTime().compareTo(journey.getArrivalTime()) > 0) {
                throw new ObjectCannotBeUpdatedException("Journey");
            }
            existingJourney.setDepartureTime(journey.getDepartureTime());
            existingJourney.setArrivalTime(journey.getArrivalTime());
            return journeyRepository.save(existingJourney);
        } else {
            throw new ObjectCannotBeUpdatedException("Journey");
        }

    }

    public List<Journey> findJourneysAfterCurrentTime(){
        return journeyRepository.findByDepartureTimeGreaterThan(new Timestamp(System.currentTimeMillis()));
    }

    public List<Journey> findJourneysAfterDepartureRailwayStation(long departureRailwayStationId){
        return journeyRepository.findAllByDepartureRailwayStationId(departureRailwayStationId);
    }

    public List<Journey> findJourneysAfterArrivalRailwayStation(long arrivalRailwayStationId){
        return journeyRepository.findAllByArrivalRailwayStationId(arrivalRailwayStationId);
    }

    public List<Journey> findJourneysAfterStatus(JourneyStatus journeyStatus){
        return journeyRepository.findByJourneyStatus(journeyStatus);
    }

    public String findTotalDelay(Credentials credentials){
        checkCredentials(credentials);
        return journeyRepository.findDelay();
    }


}
