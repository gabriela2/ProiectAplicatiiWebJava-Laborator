package com.example.railwaystationapprest.repository;

import com.example.railwaystationapprest.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findAllByUserIdAndJourneyId(long userId, long journeyId);

    List<Incident> findAllByUserId(long userId);

    List<Incident> findAllByJourneyId(long journeyId);
}
