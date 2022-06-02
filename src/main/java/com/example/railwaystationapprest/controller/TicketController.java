package com.example.railwaystationapprest.controller;

import com.example.railwaystationapprest.dto.TicketDto;
import com.example.railwaystationapprest.mapper.TicketMapper;
import com.example.railwaystationapprest.model.Ticket;
import com.example.railwaystationapprest.service.Credentials;
import com.example.railwaystationapprest.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ticket")
public class TicketController {
    private TicketService ticketService;
    private TicketMapper ticketMapper;

    public TicketController(TicketService ticketService, TicketMapper ticketMapper) {
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
    }

    @PostMapping("/{journeyId}")
    public ResponseEntity<TicketDto> buyTicket(@RequestHeader(Credentials.AUTHORIZATION) String header, @PathVariable long journeyId ){
        Credentials credentials = new Credentials(header);
        Ticket savedTicket = ticketService.createTicket(journeyId, credentials);
        return ResponseEntity
                .created(URI.create("/ticket/" + savedTicket.getId())).body(ticketMapper.convertTicketToTicketDto(savedTicket));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@RequestHeader(Credentials.AUTHORIZATION) String header, @PathVariable long id){
        Credentials credentials = new Credentials(header);
        ticketService.deleteTicket(id, credentials);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<TicketDto>> getTicketsByUser(@RequestHeader(Credentials.AUTHORIZATION) String header, @PathVariable long id){
        Credentials credentials = new Credentials(header);
        List<Ticket> tickets = ticketService.findTicketsForUserId(id, credentials);
        return tickets.isEmpty()? ResponseEntity.noContent().build(): ResponseEntity.ok(tickets.stream().map(ticket -> ticketMapper.convertTicketToTicketDto(ticket)).collect(Collectors.toList()));
    }

    @GetMapping("/journey/{id}")
    public ResponseEntity<List<TicketDto>> getTicketsByJourney(@RequestHeader(Credentials.AUTHORIZATION) String header, @PathVariable long id){
        Credentials credentials = new Credentials(header);
        List<Ticket> tickets = ticketService.findTicketsForJourneyId(id, credentials);
        return tickets.isEmpty()? ResponseEntity.noContent().build(): ResponseEntity.ok(tickets.stream().map(ticket -> ticketMapper.convertTicketToTicketDto(ticket)).collect(Collectors.toList()));
    }


}
