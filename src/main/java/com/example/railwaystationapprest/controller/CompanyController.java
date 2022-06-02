package com.example.railwaystationapprest.controller;

import com.example.railwaystationapprest.dto.CompanyDto;
import com.example.railwaystationapprest.mapper.CompanyMapper;
import com.example.railwaystationapprest.model.Company;
import com.example.railwaystationapprest.service.CompanyService;
import com.example.railwaystationapprest.service.Credentials;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/company")
@Validated
public class CompanyController {
    private CompanyService companyService;
    private CompanyMapper companyMapper;

    public CompanyController(CompanyService companyService, CompanyMapper companyMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestHeader(Credentials.AUTHORIZATION) String header, @Valid @RequestBody CompanyDto companyDto){
        Credentials credentials = new Credentials(header);
        Company savedCompany = companyService.createCompany(companyMapper.convertCompanyDtoToCompany(companyDto), credentials);
        return ResponseEntity.created(URI.create("/company/" + savedCompany.getId())).body(savedCompany);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@RequestHeader(Credentials.AUTHORIZATION) String header, @PathVariable long id){
        Credentials credentials = new Credentials(header);
        companyService.deleteCompany(id, credentials);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CompanyDto>> getCompanies(@RequestHeader(Credentials.AUTHORIZATION) String header){
        Credentials credentials = new Credentials(header);
        List<Company> companies = companyService.getCompanies(credentials);
        return companies.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(companies.stream().map(company -> companyMapper.convertCompanyToCompanyDto(company)).collect(Collectors.toList()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@RequestHeader(Credentials.AUTHORIZATION) String header, @PathVariable long id){
        Credentials credentials = new Credentials(header);
        return ResponseEntity.ok(companyMapper.convertCompanyToCompanyDto(companyService.getCompanyById(credentials, id)));
    }

    @PostMapping("/{idCompany}/license/{idLicense}")
    public ResponseEntity<Void> addCompanyLicense(@RequestHeader(Credentials.AUTHORIZATION)String header, @PathVariable("idCompany") long idCompany,@PathVariable("idLicense") long idLicense){
        Credentials credentials = new Credentials(header);
        companyService.createCompanyLicense(credentials,idCompany,idLicense);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idCompany}/license/{idLicense}")
    public ResponseEntity<Void> deleteCompanyJourney(@RequestHeader(Credentials.AUTHORIZATION)String header, @PathVariable("idCompany") long idCompany,@PathVariable("idLicense") long idLicense){
        Credentials credentials = new Credentials(header);
        companyService.deleteCompanyLicense(credentials,idCompany,idLicense);
        return ResponseEntity.noContent().build();
    }
}
