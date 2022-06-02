package com.example.railwaystationapprest.dto;

import com.example.railwaystationapprest.model.RailwayType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RailwayStationDto {
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotNull
    private RailwayType railwayType;
    @Size(max = 10)
    @NotBlank
    private String number;
    @NotBlank
    private String street;
    @NotBlank
    private String city;
    @NotBlank
    @Size(max = 30)
    private String district;
    @NotBlank
    @Size(max = 10)
    private String zipcode;

    public RailwayStationDto() {
    }

    public RailwayStationDto(String name, RailwayType railwayType, String number, String street, String city, String district, String zipcode) {
        this.name = name;
        this.railwayType = railwayType;
        this.number = number;
        this.street = street;
        this.city = city;
        this.district = district;
        this.zipcode = zipcode;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
