package com.example.railwaystationapprest.service;

import com.example.railwaystationapprest.exception.BadCredentialsException;
import com.example.railwaystationapprest.exception.ObjectNotFoundException;
import com.example.railwaystationapprest.model.Company;
import com.example.railwaystationapprest.model.Journey;
import com.example.railwaystationapprest.model.JourneyStatus;
import com.example.railwaystationapprest.model.User;
import com.example.railwaystationapprest.repository.JourneyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class JourneyServiceTest {

    @Mock
    JourneyRepository journeyRepository;
    @InjectMocks
    JourneyService journeyService;


    @Test
    void getJourneysByTrainId() {
        List<Journey> journeyList = new ArrayList<>();
        when(journeyRepository.findAllByTrainId(1l)).thenReturn(journeyList);
        List<Journey> result = journeyService.getJourneysByTrainId(1l);
        assertEquals(journeyList.size(),result.size());
    }

    @Test
    void getJourneysByCompanyId() {
        List<Journey> journeyList = new ArrayList<>();
        when(journeyRepository.findAllByCompanyId(anyLong())).thenReturn(journeyList);
        List<Journey> result = journeyService.getJourneysByCompanyId(anyLong());
        assertEquals(journeyList.size(),result.size());
    }

    @Test
    void findJourneyByIdNotFound() {
        Journey journey= new Journey();
        when(journeyRepository.findById(anyLong())).thenReturn(Optional.empty());
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()->journeyService.findJourneyById(anyLong()));
        assertEquals("journey does not exist",exception.getMessage());
    }

    @Test
    void findJourneyByIdFound() {
        Journey journey= new Journey();
        journey.setJourneyStatus(JourneyStatus.ARRIVED);
        when(journeyRepository.findById(anyLong())).thenReturn(Optional.of(journey));
        Journey result= journeyService.findJourneyById(1l);
        assertEquals(journey.getJourneyStatus(),result.getJourneyStatus());
    }
    @Test
    void findJourneysAfterCurrentTime(){
        List<Journey> journeys = new ArrayList<>();
        when(journeyRepository.findByDepartureTimeGreaterThan(any())).thenReturn(journeys);
        List<Journey> result = journeyService.findJourneysAfterCurrentTime();
        assertEquals(journeys.size(),result.size());
    }

    @Test
    void findJourneysAfterDepartureRailwayStation(){
        List<Journey> journeys = new ArrayList<>();
        when(journeyRepository.findAllByDepartureRailwayStationId(1l)).thenReturn(journeys);
        List<Journey> result = journeyService.findJourneysAfterDepartureRailwayStation(1l);
        assertEquals(journeys.size(),result.size());
    }

    @Test
    void findJourneysAfterArrivalRailwayStation(){
        List<Journey> journeys = new ArrayList<>();
        when(journeyRepository.findAllByArrivalRailwayStationId(1l)).thenReturn(journeys);
        List<Journey> result = journeyService.findJourneysAfterArrivalRailwayStation(1l);
        assertEquals(journeys.size(),result.size());
    }

    @Test
    void findJourneysAfterStatus(){
        List<Journey> journeys = new ArrayList<>();
        when(journeyRepository.findByJourneyStatus(any())).thenReturn(journeys);
        List<Journey> result = journeyService.findJourneysAfterStatus(JourneyStatus.ARRIVED);
        assertEquals(journeys.size(),result.size());
    }

    @Test
    void findTotalDelayBadCredentials(){
        User adminUser = new User();
        adminUser.setEmail("test@railway-transport.ro");
        adminUser.setPassword("test");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        //act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()->journeyService.findTotalDelay(credentials));
        //assert
        assertNotNull(exception);
        assertEquals("The credentials used are not correct",exception.getMessage());

    }

    @Test
    void findTotalDelay(){
        ReflectionTestUtils.setField(journeyService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(journeyService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        when(journeyRepository.findDelay()).thenReturn("1");
        String delay = journeyService.findTotalDelay(credentials);
        assertEquals("1",delay);
    }
}