package com.example.railwaystationapprest.repository;

import com.example.railwaystationapprest.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {
    List<Ticket> findAllByUserId(long userId);

    List<Ticket> findAllByJourneyId(long journeyId);
}
