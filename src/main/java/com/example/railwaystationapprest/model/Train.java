package com.example.railwaystationapprest.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "code", nullable = false, length = 20, unique = true)
    private String code;
    @Column(name = "number_of_seats", nullable = false)
    private int numberOfSeats;
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;
    @Enumerated(EnumType.STRING)
    private TrainType trainType;
    @OneToMany(mappedBy = "train")
    private List<Journey> journeys;

    public Train() {
    }

    public Train(String code, int numberOfSeats, FuelType fuelType, TrainType trainType) {
        this.code = code;
        this.numberOfSeats = numberOfSeats;
        this.fuelType = fuelType;
        this.trainType = trainType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public TrainType getTrainType() {
        return trainType;
    }

    public void setTrainType(TrainType trainType) {
        this.trainType = trainType;
    }

    public List<Journey> getJourneys() {
        return journeys;
    }

    public void setJourneys(List<Journey> journeys) {
        this.journeys = journeys;
    }
}
