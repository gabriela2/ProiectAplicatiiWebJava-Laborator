package com.example.railwaystationapprest.service;

import com.example.railwaystationapprest.exception.*;
import com.example.railwaystationapprest.model.Journey;
import com.example.railwaystationapprest.model.License;
import com.example.railwaystationapprest.model.Train;
import com.example.railwaystationapprest.model.User;
import com.example.railwaystationapprest.repository.TrainRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainServiceTest {

    @Mock
    TrainRepository trainRepository;
    @Mock
    JourneyService journeyService;
    @InjectMocks
    TrainService trainService;

    @Test
    void getTrainByCodeNotOk() {
        //arrange
        Train train = new Train();
        train.setCode("1234");
        when(trainRepository.findByCode(anyString())).thenReturn(Optional.empty());
        //act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()->trainService.getTrainByCode("a"));
        //assert
        assertEquals("Train does not exist",exception.getMessage());
    }

    @Test
    void getTrainByCodeOk() {
        //arrange
        Train train = new Train();
        train.setCode("1234");
        when(trainRepository.findByCode(anyString())).thenReturn(Optional.of(train));
        //act
        Train result = trainService.getTrainByCode("a");
        //assert
        assertEquals(train.getCode(),result.getCode());
    }

    @Test
    void getTrainsCredentialsNotOk() {
        User adminUser = new User();
        adminUser.setEmail("test@railway-transport.ro");
        adminUser.setPassword("test");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        //act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()->trainService.getTrains(credentials));
        //assert
        assertNotNull(exception);
        assertEquals("The credentials used are not correct",exception.getMessage());

    }

    @Test
    void getTrainsCredentialsOk() {
        ReflectionTestUtils.setField(trainService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(trainService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        List<Train> trains = new ArrayList<>();
        when(trainRepository.findAll()).thenReturn(trains);
        //act
        List<Train> result =trainService.getTrains(credentials);
        //assert
        assertEquals(trains.size(),result.size());
    }

    @Test
    void getTrainByIdNotFound() {
        ReflectionTestUtils.setField(trainService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(trainService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Train train = new Train();
        train.setId(1);
        when(trainRepository.findById(1l)).thenReturn(Optional.empty());
        //act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()->trainService.getTrainById(credentials,1l));
        //assert
        assertEquals("Train does not exist",exception.getMessage());
    }

    @Test
    void getTrainByIFound() {
        ReflectionTestUtils.setField(trainService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(trainService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Train train = new Train();
        train.setId(1);
        when(trainRepository.findById(1l)).thenReturn(Optional.of(train));
        //act
        Train result = trainService.getTrainById(credentials,1l);
        //assert
        assertEquals(train.getId(),result.getId());
    }


    @Test
    void createTrainPresent() {
        ReflectionTestUtils.setField(trainService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(trainService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Train train = new Train();
        train.setId(1);
        train.setCode("1234");
        when(trainRepository.findByCode(anyString())).thenReturn(Optional.of(train));
        //act
        ObjectAlreadyExistsException exception = assertThrows(ObjectAlreadyExistsException.class,()->trainService.create(train,credentials));
        //assert
        assertEquals("Train already exists in the database",exception.getMessage());
    }

    @Test
    void createTrainNotPresent() {
        ReflectionTestUtils.setField(trainService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(trainService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Train train = new Train();
        train.setId(1);
        train.setCode("1234");
        when(trainRepository.findByCode(anyString())).thenReturn(Optional.empty());
        when(trainRepository.save(train)).thenReturn(train);
        //act
        Train result = trainService.create(train,credentials);
        //assert
        assertEquals(train.getCode(),result.getCode());
        verify(trainRepository,times(1)).save(train);
    }

    @Test
    void updateTrainNotPresent() {
        ReflectionTestUtils.setField(trainService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(trainService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Train train = new Train();
        train.setId(1);
        train.setCode("1234");
        when(trainRepository.findById(anyLong())).thenReturn(Optional.empty());
        //act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()->trainService.updateTrain(train,credentials));
        //assert
        assertEquals("Train does not exist",exception.getMessage());
    }

    @Test
    void updateTrainPresentCodeNotOk() {
        ReflectionTestUtils.setField(trainService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(trainService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Train train = new Train();
        train.setId(1);
        train.setCode("1234");
        Train train1 = new Train();
        train1.setCode("abc");
        when(trainRepository.findById(anyLong())).thenReturn(Optional.of(train1));
        //act
        ObjectCannotBeUpdatedException exception = assertThrows(ObjectCannotBeUpdatedException.class,()->trainService.updateTrain(train,credentials));
        //assert
        assertEquals("The code cannot be changed",exception.getMessage());
    }

    @Test
    void updateTrainPresentCodeOk() {
        ReflectionTestUtils.setField(trainService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(trainService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Train train = new Train();
        train.setId(1);
        train.setCode("1234");
        when(trainRepository.findById(anyLong())).thenReturn(Optional.of(train));
        when(trainRepository.save(train)).thenReturn(train);
        //act
        Train result = trainService.updateTrain(train,credentials);
        //assert
        assertEquals(train.getCode(),result.getCode());
    }


    @Test
    void deleteTrainNotPresent() {
        ReflectionTestUtils.setField(trainService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(trainService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Train train = new Train();
        train.setId(1);
        train.setCode("1234");
        when(trainRepository.findById(anyLong())).thenReturn(Optional.empty());
        //act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()->trainService.deleteTrain(1l,credentials));
        //assert
        assertEquals("Train does not exist",exception.getMessage());
    }

    @Test
    void deleteTrainPresentJourneyEmpty() {
        ReflectionTestUtils.setField(trainService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(trainService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Train train = new Train();
        train.setId(1);
        train.setCode("1234");
        List<Journey> journeys = new ArrayList<>();
        when(trainRepository.findById(anyLong())).thenReturn(Optional.of(train));
        when(journeyService.getJourneysByTrainId(1l)).thenReturn(journeys);
        //act
        trainService.deleteTrain(1,credentials);
        //assert
        verify(trainRepository,times(1)).deleteById(1l);
    }

    @Test
    void deleteTrainPresentJourneyNotEmpty() {
        ReflectionTestUtils.setField(trainService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(trainService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Train train = new Train();
        train.setId(1);
        train.setCode("1234");
        List<Journey> journeys = new ArrayList<>();
        Journey journey = new Journey();
        journeys.add(journey);
        when(trainRepository.findById(anyLong())).thenReturn(Optional.of(train));
        when(journeyService.getJourneysByTrainId(1l)).thenReturn(journeys);
        //act
        ObjectCannotBeDeletedException exception = assertThrows(ObjectCannotBeDeletedException.class,()->trainService.deleteTrain(1,credentials));
        //assert
        assertEquals("Train cannot be deleted",exception.getMessage());
    }
}