package com.example.railwaystationapprest.controller;

import com.example.railwaystationapprest.dto.IncidentDto;
import com.example.railwaystationapprest.mapper.IncidentMapper;
import com.example.railwaystationapprest.model.Incident;
import com.example.railwaystationapprest.model.IncidentStatus;
import com.example.railwaystationapprest.service.Credentials;
import com.example.railwaystationapprest.service.IncidentService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/incident")
@Validated
public class IncidentController {
    private IncidentService incidentService;
    private IncidentMapper incidentMapper;

    public IncidentController(IncidentService incidentService,IncidentMapper incidentMapper) {
        this.incidentService = incidentService;
        this.incidentMapper = incidentMapper;
    }

    @PostMapping()
    public ResponseEntity<IncidentDto> createIncident(@RequestHeader(Credentials.AUTHORIZATION) String header, @Valid @RequestBody IncidentDto incidentDto){
        Credentials credentials = new Credentials(header);
        Incident savedIncident = incidentService.createIncident(credentials,incidentMapper.convertIncidentDtoToIncident(incidentDto));
        return ResponseEntity
                .created(URI.create("/incident/" + savedIncident.getId())).body(incidentMapper.convertIncidentToIncidentDto(savedIncident));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@RequestHeader(Credentials.AUTHORIZATION) String header, @PathVariable long id){
        Credentials credentials = new Credentials(header);
        incidentService.delete(id, credentials);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<IncidentDto>> getIncidentsByUser(@RequestHeader(Credentials.AUTHORIZATION) String header, @PathVariable long id){
        Credentials credentials = new Credentials(header);
        List<Incident> incidents = incidentService.findIncidentsForUserId(id, credentials);
        return incidents.isEmpty()? ResponseEntity.noContent().build(): ResponseEntity.ok(incidents.stream().map(incident -> incidentMapper.convertIncidentToIncidentDto(incident)).collect(Collectors.toList()));
    }

    @GetMapping("/journey/{id}")
    public ResponseEntity<List<IncidentDto>> getIncidentsByJourney(@RequestHeader(Credentials.AUTHORIZATION) String header, @PathVariable long id){
        Credentials credentials = new Credentials(header);
        List<Incident> incidents = incidentService.findIncidentsForJourneyId(id, credentials);
        return incidents.isEmpty()? ResponseEntity.noContent().build(): ResponseEntity.ok(incidents.stream().map(incident -> incidentMapper.convertIncidentToIncidentDto(incident)).collect(Collectors.toList()));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchStatus(@RequestHeader(Credentials.AUTHORIZATION) String header, @PathVariable long id, @RequestParam(required = true) IncidentStatus incidentStatus){
        Credentials credentials = new Credentials(header);
        incidentService.patchStatus(credentials,id,incidentStatus);
        return ResponseEntity.noContent().build();
    }
}
