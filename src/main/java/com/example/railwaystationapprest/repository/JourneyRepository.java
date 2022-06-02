package com.example.railwaystationapprest.repository;

import com.example.railwaystationapprest.model.Journey;
import com.example.railwaystationapprest.model.JourneyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface JourneyRepository extends JpaRepository<Journey,Long> {
    List<Journey> findAllByCompanyId(long companyId);
    List<Journey> findAllByTrainId(long trainId);
    List<Journey> findByJourneyStatus(JourneyStatus status);

    @Query(value = "select * from journey where company_id=:company_id and departure_time=:departureTime and arrival_time=:arrivalTime and departure_railway_station_id=:departure_railway_station_id and arrival_railway_station_id=:arrival_railway_station_id and train_id=:train_id", nativeQuery = true)
    Optional<Journey> findJourney(long company_id, Timestamp departureTime, Timestamp arrivalTime, long train_id, long departure_railway_station_id, long arrival_railway_station_id);

    List<Journey> findAllByDepartureRailwayStationId(long departureRailwayStationId);

    List<Journey> findAllByArrivalRailwayStationId(long arrivalRailwayStationId);

    List<Journey> findByDepartureTimeGreaterThan(Timestamp departureTime);

    @Query(value = "select sum(minute_late) from journey",nativeQuery = true)
    String findDelay();
}
