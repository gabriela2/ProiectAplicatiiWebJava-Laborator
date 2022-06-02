package com.example.railwaystationapprest.dto;

import com.example.railwaystationapprest.model.JourneyStatus;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class JourneyDto {
    @NotNull
    @Min(0)
    private int distance;
    @NotNull
    private Timestamp departureTime;
    @NotNull
    private Timestamp arrivalTime;
    private int minuteLate;
    @NotNull
    @Min(0)
    private double ticketPrice;
    @NotNull
    private JourneyStatus journeyStatus;
    @NotNull
    private String trainCode;
    @NotNull
    private String companyName;
    @NotNull
    private String departureRailway;
    @NotNull
    private String arrivalRailway;

    public JourneyDto() {
    }

    public JourneyDto(int distance, Timestamp departureTime, Timestamp arrivalTime, int minuteLate, double ticketPrice, JourneyStatus journeyStatus, String trainCode, String companyName, String departureRailway, String arrivalRailway) {
        this.distance = distance;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.minuteLate = minuteLate;
        this.ticketPrice = ticketPrice;
        this.journeyStatus = journeyStatus;
        this.trainCode = trainCode;
        this.companyName = companyName;
        this.departureRailway = departureRailway;
        this.arrivalRailway = arrivalRailway;
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

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartureRailway() {
        return departureRailway;
    }

    public void setDepartureRailway(String departureRailway) {
        this.departureRailway = departureRailway;
    }

    public String getArrivalRailway() {
        return arrivalRailway;
    }

    public void setArrivalRailway(String arrivalRailway) {
        this.arrivalRailway = arrivalRailway;
    }
}
