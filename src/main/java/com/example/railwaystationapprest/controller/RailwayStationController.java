package com.example.railwaystationapprest.controller;

import com.example.railwaystationapprest.dto.RailwayStationDto;
import com.example.railwaystationapprest.mapper.RailwayStationMapper;
import com.example.railwaystationapprest.model.RailwayStation;
import com.example.railwaystationapprest.service.Credentials;
import com.example.railwaystationapprest.service.RailwayStationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/railwaystation")
@Validated
public class RailwayStationController {
    private RailwayStationService railwayStationService;
    private RailwayStationMapper railwayStationMapper;

    public RailwayStationController(RailwayStationService railwayStationService, RailwayStationMapper railwayStationMapper) {
        this.railwayStationService = railwayStationService;
        this.railwayStationMapper = railwayStationMapper;
    }

    @PostMapping
    public ResponseEntity<RailwayStation> createRailwayStation(@RequestHeader(Credentials.AUTHORIZATION) String header, @Valid @RequestBody RailwayStationDto railwayStationDto){
        Credentials credentials = new Credentials(header);
        RailwayStation savedRailwayStation = railwayStationService.createRailwayStation(railwayStationMapper.convertRailwayStationDtoToRailwayStation(railwayStationDto), credentials);
        return ResponseEntity
                .created(URI.create("/railwaystation/" + savedRailwayStation.getId()))
                .body(savedRailwayStation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRailwayStation(@RequestHeader(Credentials.AUTHORIZATION) String header, @PathVariable long id){
        Credentials credentials = new Credentials(header);
        railwayStationService.deleteRailwayStation(id, credentials);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RailwayStationDto> getRailwayStationById(@PathVariable long id){
        RailwayStation railwayStation = railwayStationService.getRailwayStationById(id);
        RailwayStationDto railwayStationDto = railwayStationMapper.convertRailwayStationToRailwayStationDto(railwayStation);
        return ResponseEntity.ok(railwayStationDto);
    }

    @GetMapping
    public ResponseEntity<List<RailwayStationDto>> getRailwayStations(
            @RequestParam(value = "sort",required = false) String sort,
            @RequestParam(value = "addressComponent", required = false) String addressComponent,
            @RequestParam(value = "address", required = false) String address

    ){
        List<RailwayStation> railwayStations = railwayStationService.getRailwayStations(sort,addressComponent,address);
        return railwayStations.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(railwayStations
                .stream()
                .map(railwayStation -> railwayStationMapper.convertRailwayStationToRailwayStationDto(railwayStation))
                .collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RailwayStationDto> updateRailwayStation(@RequestHeader(Credentials.AUTHORIZATION) String header, @Valid @RequestBody RailwayStationDto updateRailwayStationDto, @PathVariable long id) {
        Credentials credentials = new Credentials(header);
        RailwayStation railwayStation = railwayStationMapper.convertRailwayStationDtoToRailwayStation(updateRailwayStationDto);
        return ResponseEntity.ok(railwayStationMapper.convertRailwayStationToRailwayStationDto(railwayStationService.update(id,railwayStation, credentials)));
    }


}
