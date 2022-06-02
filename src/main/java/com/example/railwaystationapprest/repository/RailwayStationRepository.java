package com.example.railwaystationapprest.repository;

import com.example.railwaystationapprest.model.RailwayStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RailwayStationRepository extends JpaRepository<RailwayStation,Long> {
    Optional<RailwayStation> getByName(String name);
    @Query(value = "select * from railway_station rs join address adr on rs.address_id = adr.id where city=:address",nativeQuery = true)
    List<RailwayStation> findAllByCity(String address);

    @Query(value = "select * from railway_station rs join address adr on rs.address_id = adr.id where district=:address",nativeQuery = true)
    List<RailwayStation> findAllByDistrict(String address);

}
