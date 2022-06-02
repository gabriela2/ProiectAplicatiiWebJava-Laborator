package com.example.railwaystationapprest.service;

import com.example.railwaystationapprest.exception.*;
import com.example.railwaystationapprest.model.License;
import com.example.railwaystationapprest.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LicenseService {
    private LicenseRepository licenseRepository;

    public LicenseService(LicenseRepository licenseRepository) {
        this.licenseRepository = licenseRepository;
    }

    @Value("${administrator.email}")
    private String adminEmail;
    @Value("${administrator.password}")
    private String adminPassword;

    private void checkCredentials(Credentials credentials) {
        if(!(credentials.getEmail().equals(adminEmail) && credentials.getPassword().equals(adminPassword))){
            throw new BadCredentialsException();
        }
    }

    public Optional<License> getLicenseById(long idLicense) {
        return licenseRepository.findById(idLicense);
    }

    public List<License> getLicenses(Credentials credentials) {
        checkCredentials(credentials);
        return licenseRepository.findAll();

    }

    public License getLicenseUsingId(Credentials credentials, long id) {
        checkCredentials(credentials);
        License license = licenseRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("License"));
        return license;

    }

    public License createLicense(License license, Credentials credentials) {
        checkCredentials(credentials);
        Optional<License> existingLicense = licenseRepository.findByLicenseNumber(license.getLicenseNumber());
        if(existingLicense.isPresent()){
            throw new ObjectAlreadyExistsException("License");
        }
        return licenseRepository.save(license);

    }

    public void deleteLicense(long id, Credentials credentials) {
        checkCredentials(credentials);
        License licenseToBeDeleted = licenseRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("License"));
        licenseRepository.delete(licenseToBeDeleted);

    }

    public License updateLicense(License license, Credentials credentials) {
        checkCredentials(credentials);
        License existingLicense = licenseRepository.findById(license.getId()).orElseThrow(()-> new ObjectNotFoundException("License"));
        if(!existingLicense.getLicenseNumber().equals(license.getLicenseNumber())){
            throw new ObjectCannotBeUpdatedException("License");
        }
        return licenseRepository.save(license);
    }
}
