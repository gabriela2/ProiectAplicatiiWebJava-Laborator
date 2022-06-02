package com.example.railwaystationapprest.mapper;

import com.example.railwaystationapprest.dto.LicenseDto;
import com.example.railwaystationapprest.model.License;
import org.springframework.stereotype.Component;

@Component
public class LicenseMapper {
    public License convertLicenseDtoToLicense(LicenseDto licenseDto){
        return new License(licenseDto.getLicenseNumber(),licenseDto.getLicenceDescription());
    }

    public LicenseDto convertLicenseToLicenseDto(License license){
        return new LicenseDto(license.getLicenseNumber(),license.getLicenseDescription());
    }
}
