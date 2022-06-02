package com.example.railwaystationapprest.dto;

import com.example.railwaystationapprest.model.FuelType;
import com.example.railwaystationapprest.model.TrainType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TrainDto {
    @NotBlank
    @Size(max = 20)
    private String code;
    @NotNull
    private int numberOfSeats;
    @NotNull
    private FuelType fuelType;
    @NotNull
    private TrainType trainType;

    public TrainDto() {
    }

    public TrainDto(String code, int numberOfSeats, FuelType fuelType, TrainType trainType) {
        this.code = code;
        this.numberOfSeats = numberOfSeats;
        this.fuelType = fuelType;
        this.trainType = trainType;
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
}
