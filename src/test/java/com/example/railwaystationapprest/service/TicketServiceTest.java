package com.example.railwaystationapprest.service;

import com.example.railwaystationapprest.exception.BadCredentialsException;
import com.example.railwaystationapprest.exception.ObjectAlreadyExistsException;
import com.example.railwaystationapprest.model.Address;
import com.example.railwaystationapprest.model.Ticket;
import com.example.railwaystationapprest.model.User;
import com.example.railwaystationapprest.repository.TicketRepository;
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
class TicketServiceTest {

    @Mock
    TicketRepository ticketRepository;
    @Mock
    UserService userService;
    @Mock
    JourneyService journeyService;

    @InjectMocks
    TicketService ticketService;


    @Test
    void findTicketsForJourneyIdCredentialsNotOk() {
        User adminUser = new User();
        adminUser.setEmail("test@railway-transport.ro");
        adminUser.setPassword("test");
        Address address = new Address("12A","Street","City","District","18348a");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        //act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()->ticketService.findTicketsForJourneyId(1l,credentials));
        //assert
        assertNotNull(exception);
        assertEquals("The credentials used are not correct",exception.getMessage());
    }

    @Test
    void findTicketsForJourneyIdCredentialsOk() {
        ReflectionTestUtils.setField(ticketService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(ticketService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        List<Ticket> list = new ArrayList<>();
        when(ticketRepository.findAllByJourneyId(anyLong())).thenReturn(list);
        //act
        List<Ticket> result = ticketService.findTicketsForJourneyId(anyLong(),credentials);
        //assert
        assertEquals(list.size(),result.size());
    }

    @Test
    void findTicketsForUserIdCredentialsNotOk(){
        User adminUser = new User();
        adminUser.setEmail("ana");
        adminUser.setPassword("ana");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        User user = new User();
        user.setId(1l);
        user.setEmail("test");
        user.setPassword("test");
        when(userService.findById(anyLong())).thenReturn(user);
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()-> ticketService.findTicketsForUserId(anyLong(),credentials));
        assertEquals("The credentials used are not correct",exception.getMessage());

    }

    @Test
    void findTicketsForUserIdCredentialsOk(){
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
        List<Ticket> tickets = new ArrayList<>();
        when(userService.findById(anyLong())).thenReturn(user);
        when(ticketRepository.findAllByUserId(anyLong())).thenReturn(tickets);
        List<Ticket> result = ticketService.findTicketsForUserId(anyLong(),credentials);
        assertEquals(tickets.size(),result.size());

    }

}