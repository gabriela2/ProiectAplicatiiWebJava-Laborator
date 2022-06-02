package com.example.railwaystationapprest.service;

import com.example.railwaystationapprest.exception.BadCredentialsException;
import com.example.railwaystationapprest.exception.ObjectNotFoundException;
import com.example.railwaystationapprest.model.Incident;
import com.example.railwaystationapprest.model.IncidentStatus;
import com.example.railwaystationapprest.model.User;
import com.example.railwaystationapprest.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentService {
    private IncidentRepository incidentRepository;
    private UserService userService;


    public IncidentService(IncidentRepository incidentRepository, UserService userService) {
        this.incidentRepository = incidentRepository;
        this.userService = userService;
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

    public void delete(long id, Credentials credentials) {
        Incident existingIncident = incidentRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Incident"));
        User user = userService.findById(existingIncident.getUser().getId());
        if(!(user.getEmail().equals(credentials.getEmail()) && user.getPassword().equals(credentials.getPassword()))){
            throw new BadCredentialsException();
        }
        incidentRepository.delete(existingIncident);
    }

    public List<Incident> findIncidentsForUserId(long userId, Credentials credentials) {
        User user = userService.findById(userId);
        if(!(user.getEmail().equals(credentials.getEmail()) && user.getPassword().equals(credentials.getPassword()))){
            throw new BadCredentialsException();
        }
        return incidentRepository.findAllByUserId(userId);
    }

    public List<Incident> findIncidentsForJourneyId(long journeyId, Credentials credentials) {
        checkCredentials(credentials);
        return incidentRepository.findAllByJourneyId(journeyId);
    }

    public Incident createIncident (Credentials credentials, Incident incident){
        User user = userService.findById(incident.getUser().getId());
        if(!(user.getEmail().equals(credentials.getEmail()) && user.getPassword().equals(credentials.getPassword()))){
            throw new BadCredentialsException();
        }
        return incidentRepository.save(incident);
    }

    public void patchStatus(Credentials credentials, long id, IncidentStatus incidentStatus) {
        checkCredentials(credentials);
        Incident incident = incidentRepository.findById(id).orElseThrow(()->new ObjectNotFoundException("Incident"));
        incident.setStatus(incidentStatus);
        incidentRepository.save(incident);
    }

}
