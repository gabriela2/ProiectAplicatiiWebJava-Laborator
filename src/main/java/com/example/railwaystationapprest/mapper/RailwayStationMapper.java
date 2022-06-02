package com.example.railwaystationapprest.mapper;

import com.example.railwaystationapprest.dto.RailwayStationDto;
import com.example.railwaystationapprest.model.Address;
import com.example.railwaystationapprest.model.RailwayStation;
import org.springframework.stereotype.Component;

@Component
public class RailwayStationMapper {
    public RailwayStationDto convertRailwayStationToRailwayStationDto(RailwayStation railwayStation){
        return new RailwayStationDto(railwayStation.getName(),railwayStation.getRailwayType(),railwayStation.getAddress().getNumber(),railwayStation.getAddress().getStreet(),railwayStation.getAddress().getCity(),railwayStation.getAddress().getDistrict(),railwayStation.getAddress().getZipcode());
    }

    public RailwayStation convertRailwayStationDtoToRailwayStation (RailwayStationDto railwayStationDto){
        Address address = new Address(railwayStationDto.getNumber(),railwayStationDto.getStreet(),railwayStationDto.getCity(),railwayStationDto.getDistrict(),railwayStationDto.getZipcode());
        return new RailwayStation(railwayStationDto.getName(),railwayStationDto.getRailwayType(),address);
    }
}
