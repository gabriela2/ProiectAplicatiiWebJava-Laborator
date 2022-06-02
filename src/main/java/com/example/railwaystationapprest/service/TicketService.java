package com.example.railwaystationapprest.service;

import com.example.railwaystationapprest.exception.*;
import com.example.railwaystationapprest.model.Journey;
import com.example.railwaystationapprest.model.Ticket;
import com.example.railwaystationapprest.model.User;
import com.example.railwaystationapprest.model.UserType;
import com.example.railwaystationapprest.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private TicketRepository ticketRepository;
    private UserService userService;
    private JourneyService journeyService;

    public TicketService(TicketRepository ticketRepository, UserService userService,JourneyService journeyService) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
        this.journeyService = journeyService;
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

    @Transactional
    public Ticket createTicket(long journeyId, Credentials credentials) {
        User user = userService.findByEmail(credentials.getEmail());
        Journey journey = journeyService.findJourneyById(journeyId);
        if(!(user.getEmail().equals(credentials.getEmail()) && user.getPassword().equals(credentials.getPassword()))){
            throw new BadCredentialsException();
        }
        Double finalPrice = computeFinalPrice(user,journey.getTicketPrice());
        if (finalPrice > user.getBalance()) {
            throw new BalanceUpdateException();
        } else {
            if (journey.getTickets().size() < journey.getTrain().getNumberOfSeats()) {
                userService.patchBalance(credentials,user.getId(), -finalPrice);
                Ticket ticket = new Ticket(finalPrice, user, journey);
                return ticketRepository.save(ticket);
            } else {
                throw new ObjectCannotBeCreatedException("Ticket");
            }
        }


    }

    @Transactional
    public void deleteTicket(long id, Credentials credentials) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Ticket"));
        User user = userService.findById(ticket.getUser().getId());
        if(!(user.getEmail().equals(credentials.getEmail()) && user.getPassword().equals(credentials.getPassword()))){
            throw new BadCredentialsException();
        }
        if(ticket.getJourney().getDepartureTime().compareTo(Timestamp.valueOf(LocalDateTime.now()))>0) {
            userService.patchBalance(credentials, user.getId(), ticket.getFinalPrice());
            ticketRepository.delete(ticket);
        }else {
            throw new ObjectCannotBeDeletedException("Ticket");
        }
    }


    private Double computeFinalPrice(User user, double ticketPrice) {
        if (user.getUserType() == UserType.STUDENT) {
            return ticketPrice * 0.5;
        } else if (user.getUserType() == UserType.RETIRED) {
            return ticketPrice * 0.25;
        } else if (user.getUserType() == UserType.SOCIAL) {
            return ticketPrice * 0.75;
        } else if (user.getUserType() == UserType.MILITARY) {
            return ticketPrice * 0;
        } else {
            return ticketPrice;
        }
    }

    public List<Ticket> findTicketsForUserId(long userId, Credentials credentials) {
        User user = userService.findById(userId);
        if(!(user.getEmail().equals(credentials.getEmail()) && user.getPassword().equals(credentials.getPassword()))){
            throw new BadCredentialsException();
        }
        return ticketRepository.findAllByUserId(userId);
    }

    public List<Ticket> findTicketsForJourneyId(long journeyId, Credentials credentials) {
        checkCredentials(credentials);
        return ticketRepository.findAllByJourneyId(journeyId);
    }
}
