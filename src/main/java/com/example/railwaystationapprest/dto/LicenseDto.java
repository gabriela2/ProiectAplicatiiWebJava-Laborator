package com.example.railwaystationapprest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LicenseDto {
    @NotBlank()
    @Size(max = 20)
    private String licenseNumber;
    @NotBlank()
    private String licenceDescription;

    public LicenseDto() {
    }

    public LicenseDto(String licenseNumber, String licenceDescription) {
        this.licenseNumber = licenseNumber;
        this.licenceDescription = licenceDescription;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getLicenceDescription() {
        return licenceDescription;
    }

    public void setLicenceDescription(String licenceDescription) {
        this.licenceDescription = licenceDescription;
    }
}
