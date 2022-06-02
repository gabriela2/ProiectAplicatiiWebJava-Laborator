package com.example.railwaystationapprest.service;

import com.example.railwaystationapprest.exception.*;
import com.example.railwaystationapprest.model.Company;
import com.example.railwaystationapprest.model.License;
import com.example.railwaystationapprest.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private CompanyRepository companyRepository;
    private JourneyService journeyService;
    private LicenseService licenseService;

    public CompanyService(CompanyRepository companyRepository, JourneyService journeyService, LicenseService licenseService) {
        this.companyRepository = companyRepository;
        this.journeyService = journeyService;
        this.licenseService = licenseService;
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

    public Company createCompany(Company company, Credentials credentials) {
        checkCredentials(credentials);
        Optional<Company> existingCompany = companyRepository.findByIdentificationNumber(company.getIdentificationNumber());
        if(existingCompany.isPresent()){
            throw new ObjectAlreadyExistsException("Company");
        }
        return companyRepository.save(company);
    }

    public void deleteCompany(long id, Credentials credentials) {
        checkCredentials(credentials);
        companyRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Company"));
        if(journeyService.getJourneysByCompanyId(id).isEmpty()) {
            companyRepository.deleteById(id);
        }else {
            throw new ObjectCannotBeDeletedException("Company");
        }
    }

    public List<Company> getCompanies(Credentials credentials) {
        checkCredentials(credentials);
        return companyRepository.findAll();
    }

    public Company getCompanyById(Credentials credentials, long id) {
        checkCredentials(credentials);
        Company company = companyRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Company"));
        return company;
    }

    public void createCompanyLicense(Credentials credentials, long idCompany, long idLicense) {
        checkCredentials(credentials);
        Optional<Company> company = companyRepository.findById(idCompany);
        if(company.isPresent()){
            Optional<License> license = licenseService.getLicenseById(idLicense);
            if(license.isPresent()){
                if (company.get().getLicenses().contains(license.get())){
                    throw new ObjectCannotBeCreatedException("License - Company");
                }else{
                    company.get().addLicense(license.get());
                    companyRepository.save(company.get());
                }
            }else {
                throw new ObjectNotFoundException("License");
            }
        }else{
            throw new ObjectNotFoundException("Company");
        }

    }

    public void deleteCompanyLicense(Credentials credentials, long idCompany, long idLicense) {
        checkCredentials(credentials);
        Optional<Company> company = companyRepository.findById(idCompany);
        if(company.isPresent()){
            Optional<License> license = licenseService.getLicenseById(idLicense);
            if(license.isPresent()){
                if (company.get().getLicenses().contains(license.get())){
                    company.get().removeLicense(license.get());
                    companyRepository.save(company.get());
                }else{
                    throw new ObjectCannotBeDeletedException("Company-License");
                }
            }else {
                throw new ObjectNotFoundException("License");
            }
        }else{
            throw new ObjectNotFoundException("Company");
        }
    }

    public Company getCompanyByName(String companyName) {
        Company existingCompany = companyRepository.findByName(companyName).orElseThrow(()-> new ObjectNotFoundException("Company"));
        return existingCompany;
    }
}
