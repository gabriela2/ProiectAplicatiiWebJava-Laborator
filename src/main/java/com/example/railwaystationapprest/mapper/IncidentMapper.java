package com.example.railwaystationapprest.mapper;

import com.example.railwaystationapprest.dto.IncidentDto;
import com.example.railwaystationapprest.model.Incident;
import com.example.railwaystationapprest.model.Journey;
import com.example.railwaystationapprest.model.User;
import com.example.railwaystationapprest.service.JourneyService;
import com.example.railwaystationapprest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IncidentMapper {
    @Autowired
    private JourneyService journeyService;
    @Autowired
    private UserService userService;

    public Incident convertIncidentDtoToIncident(IncidentDto incidentDto){
        User user = userService.findById(incidentDto.getUserId());
        Journey journey = journeyService.findJourneyById(incidentDto.getJourneyId());
        return new Incident(incidentDto.getDescription(),incidentDto.getStatus(),user,journey);
    }

    public IncidentDto convertIncidentToIncidentDto(Incident incident){
        return new IncidentDto(incident.getDescription(),incident.getStatus(),incident.getUser().getId(),incident.getJourney().getId());
    }
}
