package com.example.railwaystationapprest.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class RailwayStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 100, unique = true)
    private String name;
    @Enumerated(EnumType.STRING)
    private RailwayType railwayType;
    @OneToMany(mappedBy = "arrivalRailwayStation")
    private List<Journey> arrivalJourneys;
    @OneToMany(mappedBy = "departureRailwayStation")
    private List<Journey> departureJourneys;
    @OneToOne
    @JoinColumn(name = "address_id", unique = true)
    private Address address;

    public RailwayStation() {
    }

    public RailwayStation(String name, RailwayType railwayType, Address address) {
        this.name = name;
        this.railwayType = railwayType;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RailwayType getRailwayType() {
        return railwayType;
    }

    public void setRailwayType(RailwayType railwayType) {
        this.railwayType = railwayType;
    }

    public List<Journey> getArrivalJourneys() {
        return arrivalJourneys;
    }

    public void setArrivalJourneys(List<Journey> arrivalJourneys) {
        this.arrivalJourneys = arrivalJourneys;
    }

    public List<Journey> getDepartureJourneys() {
        return departureJourneys;
    }

    public void setDepartureJourneys(List<Journey> departureJourneys) {
        this.departureJourneys = departureJourneys;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
