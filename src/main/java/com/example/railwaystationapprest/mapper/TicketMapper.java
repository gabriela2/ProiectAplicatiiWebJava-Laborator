package com.example.railwaystationapprest.mapper;

import com.example.railwaystationapprest.dto.TicketDto;
import com.example.railwaystationapprest.model.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {
    public TicketDto convertTicketToTicketDto(Ticket ticket){
        return new TicketDto(ticket.getFinalPrice(),ticket.getUser().getId(),ticket.getJourney().getId());
    }
}
