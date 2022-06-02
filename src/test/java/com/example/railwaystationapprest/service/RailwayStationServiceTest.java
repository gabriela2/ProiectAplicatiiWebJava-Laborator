package com.example.railwaystationapprest.service;

import com.example.railwaystationapprest.exception.ObjectNotFoundException;
import com.example.railwaystationapprest.model.RailwayStation;
import com.example.railwaystationapprest.repository.RailwayStationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RailwayStationServiceTest {
    @Mock
    RailwayStationRepository railwayStationRepository;
    @Mock
    AddressService addressService;
    @InjectMocks
    RailwayStationService railwayStationService;

    @Test
    void getRailwayStationByNameNotFound() {
        RailwayStation railwayStation = new RailwayStation();
        railwayStation.setName("abc");
        when(railwayStationRepository.getByName(anyString())).thenReturn(Optional.empty());
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()->railwayStationService.getRailwayStationByName("def"));
        assertEquals("Railway station does not exist",exception.getMessage());

    }

    @Test
    void getRailwayStationByNameFound() {
        RailwayStation railwayStation = new RailwayStation();
        railwayStation.setName("abc");
        when(railwayStationRepository.getByName(anyString())).thenReturn(Optional.of(railwayStation));
        RailwayStation result=railwayStationService.getRailwayStationByName("def");
        assertEquals(railwayStation.getName(),result.getName());

    }

    @Test
    void getRailwayStationByIdNotFound() {
        RailwayStation railwayStation = new RailwayStation();
        railwayStation.setId(1l);
        when(railwayStationRepository.findById(anyLong())).thenReturn(Optional.empty());
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()->railwayStationService.getRailwayStationById(1l));
        assertEquals("Railway Station does not exist",exception.getMessage());

    }

    @Test
    void getRailwayStationByIdFound() {
        RailwayStation railwayStation = new RailwayStation();
        railwayStation.setId(1l);
        when(railwayStationRepository.findById(anyLong())).thenReturn(Optional.of(railwayStation));
        RailwayStation result=railwayStationService.getRailwayStationById(1l);
        assertEquals(railwayStation.getId(),result.getId());

    }
}