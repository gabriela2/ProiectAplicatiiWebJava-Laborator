package com.example.railwaystationapprest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CompanyDto {
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 15)
    private String identificationNumber;

    public CompanyDto() {
    }

    public CompanyDto(String name, String identificationNumber) {
        this.name = name;
        this.identificationNumber = identificationNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }
}
