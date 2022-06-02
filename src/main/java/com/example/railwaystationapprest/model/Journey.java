package com.example.railwaystationapprest.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table
public class Journey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private int distance;
    @JoinColumn(nullable = false)
    private Timestamp departureTime;
    @Column(nullable = false)
    private Timestamp arrivalTime;
    private int minuteLate;
    @Column(name = "ticket_price", nullable = false)
    private double ticketPrice;
    @Enumerated(EnumType.STRING)
    private JourneyStatus journeyStatus;
    @ManyToOne
    @JoinColumn(name = "train_id")
    private Train train;
    @ManyToOne
    @JoinColumn(name = "departure_railway_station_id")
    private RailwayStation departureRailwayStation;
    @ManyToOne
    @JoinColumn(name = "arrival_railway_station_id")
    private RailwayStation arrivalRailwayStation;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    @OneToMany(mappedBy = "journey")
    private List<Ticket> tickets;
    @OneToMany(mappedBy = "journey")
    private List<Incident> incidents;

    public Journey() {
    }

    public Journey(int distance, Timestamp departureTime, Timestamp arrivalTime, int minuteLate, double ticketPrice, JourneyStatus journeyStatus, Train train, RailwayStation departureRailwayStation, RailwayStation arrivalRailwayStation, Company company) {
        this.distance = distance;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.minuteLate = minuteLate;
        this.ticketPrice = ticketPrice;
        this.journeyStatus = journeyStatus;
        this.train = train;
        this.departureRailwayStation = departureRailwayStation;
        this.arrivalRailwayStation = arrivalRailwayStation;
        this.company = company;
    }

    public Journey(long id, int distance, Timestamp departureTime, Timestamp arrivalTime, int minuteLate, double ticketPrice, JourneyStatus journeyStatus, Train train, RailwayStation departureRailwayStation, RailwayStation arrivalRailwayStation, Company company) {
        this.id = id;
        this.distance = distance;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.minuteLate = minuteLate;
        this.ticketPrice = ticketPrice;
        this.journeyStatus = journeyStatus;
        this.train = train;
        this.departureRailwayStation = departureRailwayStation;
        this.arrivalRailwayStation = arrivalRailwayStation;
        this.company = company;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Timestamp getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Timestamp departureTime) {
        this.departureTime = departureTime;
    }

    public Timestamp getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Timestamp arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getMinuteLate() {
        return minuteLate;
    }

    public void setMinuteLate(int minuteLate) {
        this.minuteLate = minuteLate;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public JourneyStatus getJourneyStatus() {
        return journeyStatus;
    }

    public void setJourneyStatus(JourneyStatus journeyStatus) {
        this.journeyStatus = journeyStatus;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public RailwayStation getDepartureRailwayStation() {
        return departureRailwayStation;
    }

    public void setDepartureRailwayStation(RailwayStation departureRailwayStation) {
        this.departureRailwayStation = departureRailwayStation;
    }

    public RailwayStation getArrivalRailwayStation() {
        return arrivalRailwayStation;
    }

    public void setArrivalRailwayStation(RailwayStation arrivalRailwayStation) {
        this.arrivalRailwayStation = arrivalRailwayStation;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public List<Incident> getIncidents() {
        return incidents;
    }

    public void setIncidents(List<Incident> incidents) {
        this.incidents = incidents;
    }
}
