package com.example.railwaystationapprest.service;

import com.example.railwaystationapprest.exception.BadCredentialsException;
import com.example.railwaystationapprest.exception.ObjectNotFoundException;
import com.example.railwaystationapprest.model.Incident;
import com.example.railwaystationapprest.model.IncidentStatus;
import com.example.railwaystationapprest.model.Journey;
import com.example.railwaystationapprest.model.User;
import com.example.railwaystationapprest.repository.IncidentRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {
    @Mock
    IncidentRepository incidentRepository;
    @Mock
    UserService userService;
    @InjectMocks
    IncidentService incidentService;

    @Test
    void deleteIdNotOk() {
        //arrange
        User adminUser = new User();
        adminUser.setEmail("test@railway-transport.ro");
        adminUser.setPassword("test");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        when(incidentRepository.findById(anyLong())).thenReturn(Optional.empty());
        //act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()-> incidentService.delete(1,credentials));
        //assert
        assertEquals("Incident does not exist",exception.getMessage());
    }

    @Test
    void deleteIdOkUserNotOk() {
        //arrange
        User adminUser = new User();
        adminUser.setEmail("test");
        adminUser.setPassword("test");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        User user = new User();
        user.setId(1l);
        user.setEmail("ana");
        user.setPassword("ana");
        Incident incident = new Incident("dh", IncidentStatus.NEW,user,new Journey());
        incident.setId(1);
        when(incidentRepository.findById(1l)).thenReturn(Optional.of(incident));
        when(userService.findById(1l)).thenReturn(user);
        //act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()-> incidentService.delete(1l,credentials));
        //assert
        assertEquals("The credentials used are not correct",exception.getMessage());
    }

    @Test
    void deleteIdOkUserOk() {
        //arrange
        User adminUser = new User();
        adminUser.setEmail("test");
        adminUser.setPassword("test");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        User user = new User();
        user.setId(1l);
        user.setEmail("test");
        user.setPassword("test");
        Incident incident = new Incident("dh", IncidentStatus.NEW,user,new Journey());
        incident.setId(1);
        when(incidentRepository.findById(1l)).thenReturn(Optional.of(incident));
        when(userService.findById(1l)).thenReturn(user);
        //act
        incidentService.delete(1l,credentials);
        //assert
        verify(incidentRepository,times(1)).delete(incident);
    }

    @Test
    void findIncidentsForUserIdCredentialNotOK() {
        User adminUser = new User();
        adminUser.setEmail("test");
        adminUser.setPassword("test");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        User user = new User();
        user.setId(1l);
        user.setEmail("ana");
        user.setPassword("ana");
        when(userService.findById(1l)).thenReturn(user);
        //act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()->incidentService.findIncidentsForUserId(1l,credentials));
        //assert
        assertEquals("The credentials used are not correct",exception.getMessage());
    }

    @Test
    void findIncidentsForUserIdCredentialOK() {
        User adminUser = new User();
        adminUser.setEmail("test");
        adminUser.setPassword("test");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        User user = new User();
        user.setId(1l);
        user.setEmail("test");
        user.setPassword("test");
        List<Incident> incidentList = new ArrayList<>();
        when(userService.findById(1l)).thenReturn(user);
        when(incidentRepository.findAllByUserId(1l)).thenReturn(incidentList);
        //act
        List<Incident> result= incidentService.findIncidentsForUserId(1l,credentials);
        //assert
        assertEquals(incidentList.size(),result.size());
    }

    @Test
    void findIncidentsForJourneyIdCredentialNotOk() {
        User adminUser = new User();
        adminUser.setEmail("test@railway-transport.ro");
        adminUser.setPassword("test");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        //act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()->incidentService.findIncidentsForJourneyId(1l,credentials));
        //assert
        assertNotNull(exception);
        assertEquals("The credentials used are not correct",exception.getMessage());
    }

    @Test
    void findIncidentsForJourneyIdCredentialOk() {
        ReflectionTestUtils.setField(incidentService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(incidentService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        List<Incident> incidents = new ArrayList<>();
        when(incidentRepository.findAllByJourneyId(anyLong())).thenReturn(incidents);
        //act
        List<Incident> result =incidentService.findIncidentsForJourneyId(1l,credentials);
        //assert
        assertEquals(incidents.size(),result.size());

    }

    @Test
    void createIncidentCredentialNotOk() {
        User adminUser = new User();
        adminUser.setEmail("test");
        adminUser.setPassword("test");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        User user = new User();
        user.setId(1l);
        user.setEmail("ana");
        user.setPassword("ana");
        when(userService.findById(1l)).thenReturn(user);
        Incident incident = new Incident();
        incident.setUser(user);
        //act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()->incidentService.createIncident(credentials,incident));
        //assert
        assertEquals("The credentials used are not correct",exception.getMessage());
    }

    @Test
    void createIncidentCredentialOk() {
        User adminUser = new User();
        adminUser.setEmail("ana");
        adminUser.setPassword("ana");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        User user = new User();
        user.setId(1l);
        user.setEmail("ana");
        user.setPassword("ana");
        when(userService.findById(1l)).thenReturn(user);
        Incident incident = new Incident();
        incident.setId(10);
        incident.setUser(user);
        when(incidentRepository.save(incident)).thenReturn(incident);
        //act
        Incident result = incidentService.createIncident(credentials,incident);
        //assert
        assertEquals(incident.getId(),result.getId());
    }

    @Test
    void patchStatusIdNotOk() {
        ReflectionTestUtils.setField(incidentService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(incidentService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        when(incidentRepository.findById(anyLong())).thenReturn(Optional.empty());
        //act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()->incidentService.patchStatus(credentials,1,IncidentStatus.CLOSED));
        //assert
        assertEquals("Incident does not exist",exception.getMessage());

    }

    @Test
    void patchStatusIdOk() {
        ReflectionTestUtils.setField(incidentService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(incidentService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Incident incident = new Incident();
        incident.setStatus(IncidentStatus.NEW);
        when(incidentRepository.findById(anyLong())).thenReturn(Optional.of(incident));
        when(incidentRepository.save(incident)).thenReturn(incident);
        //act
        incidentService.patchStatus(credentials,1,IncidentStatus.CLOSED);
        //assert
        verify(incidentRepository,times(1)).save(incident);

    }
}