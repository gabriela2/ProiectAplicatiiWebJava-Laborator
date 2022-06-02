package com.example.railwaystationapprest.controller;

import com.example.railwaystationapprest.dto.JourneyDto;
import com.example.railwaystationapprest.mapper.JourneyMapper;
import com.example.railwaystationapprest.model.Journey;
import com.example.railwaystationapprest.model.JourneyStatus;
import com.example.railwaystationapprest.service.Credentials;
import com.example.railwaystationapprest.service.JourneyService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journey")
@Validated

public class JourneyController {
    private JourneyService journeyService;
    private JourneyMapper journeyMapper;

    public JourneyController(JourneyService journeyService, JourneyMapper journeyMapper) {
        this.journeyService = journeyService;
        this.journeyMapper = journeyMapper;
    }

    @PostMapping
    public ResponseEntity<JourneyDto> createJourney(@RequestHeader(Credentials.AUTHORIZATION) String header, @Valid @RequestBody JourneyDto journeyDto){
        Credentials credentials = new Credentials(header);
        Journey savedJourney = journeyService.createJourney(journeyMapper.convertJourneyDtoToJourney(journeyDto), credentials);

        return ResponseEntity
                .created(URI.create("/journey/" + savedJourney.getId())).body(journeyDto);

    }

    @PutMapping("/{id}")
    public ResponseEntity<JourneyDto> updateJourney(@RequestHeader(Credentials.AUTHORIZATION) String header, @Valid @RequestBody JourneyDto updateJourneyDto, @PathVariable long id) {
        Credentials credentials = new Credentials(header);
        Journey journey = journeyMapper.convertJourneyDtoToJourney(updateJourneyDto);
        return ResponseEntity.ok(journeyMapper.convertJourneyToJourneyDto(journeyService.updateJourney(id,journey, credentials)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJourney(@RequestHeader(Credentials.AUTHORIZATION) String header, @PathVariable long id){
        Credentials credentials = new Credentials(header);
        journeyService.deleteJourney(id, credentials);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/delay")
    public ResponseEntity<String> getTotalDelay(@RequestHeader(Credentials.AUTHORIZATION) String header){
        Credentials credentials= new Credentials(header);
        return ResponseEntity.ok("Delay = " + journeyService.findTotalDelay(credentials) + " minutes");
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<List<JourneyDto>> getJourneysAfterStatus(@PathVariable JourneyStatus status){
        List<Journey> journeys = journeyService.findJourneysAfterStatus(status);
        return journeys.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(journeys
                .stream()
                .map(journey-> journeyMapper.convertJourneyToJourneyDto(journey))
                .collect(Collectors.toList()));
    }

    @GetMapping("/departure/{id}")
    public ResponseEntity<List<JourneyDto>> getJourneysAfterDepartureId(@PathVariable long id){
        List<Journey> journeys = journeyService.findJourneysAfterDepartureRailwayStation(id);
        return journeys.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(journeys
                .stream()
                .map(journey-> journeyMapper.convertJourneyToJourneyDto(journey))
                .collect(Collectors.toList()));
    }

    @GetMapping("/arrival/{id}")
    public ResponseEntity<List<JourneyDto>> getJourneysAfterArrivalId(@PathVariable long id){
        List<Journey> journeys = journeyService.findJourneysAfterArrivalRailwayStation(id);
        return journeys.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(journeys
                .stream()
                .map(journey-> journeyMapper.convertJourneyToJourneyDto(journey))
                .collect(Collectors.toList()));
    }

    @GetMapping("/future")
    public ResponseEntity<List<JourneyDto>> getJourneysInFuture(){
        List<Journey> journeys = journeyService.findJourneysAfterCurrentTime();
        return journeys.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(journeys
                .stream()
                .map(journey-> journeyMapper.convertJourneyToJourneyDto(journey))
                .collect(Collectors.toList()));
    }



}
