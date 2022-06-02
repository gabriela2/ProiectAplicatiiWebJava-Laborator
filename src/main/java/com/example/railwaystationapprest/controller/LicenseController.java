package com.example.railwaystationapprest.controller;

import com.example.railwaystationapprest.dto.LicenseDto;
import com.example.railwaystationapprest.exception.InvalidUpdateException;
import com.example.railwaystationapprest.mapper.LicenseMapper;
import com.example.railwaystationapprest.model.License;
import com.example.railwaystationapprest.model.Train;
import com.example.railwaystationapprest.service.Credentials;
import com.example.railwaystationapprest.service.LicenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/license")
@Validated
public class LicenseController {
    private LicenseService licenseService;
    private LicenseMapper licenseMapper;

    public LicenseController(LicenseService licenseService, LicenseMapper licenseMapper) {
        this.licenseService = licenseService;
        this.licenseMapper = licenseMapper;
    }

    @GetMapping
    public ResponseEntity<List<LicenseDto>> getLicenses(@RequestHeader(Credentials.AUTHORIZATION)String header){
        Credentials credentials = new Credentials(header);
        List<License> licenses = licenseService.getLicenses(credentials);
        return licenses.isEmpty()? ResponseEntity.noContent().build() :
                ResponseEntity.ok(licenses.stream().map(license -> licenseMapper.convertLicenseToLicenseDto(license)).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LicenseDto> getLicenseById(@RequestHeader(Credentials.AUTHORIZATION)String header, @PathVariable long id){
        Credentials credentials = new Credentials(header);
        return ResponseEntity.ok(licenseMapper.convertLicenseToLicenseDto(licenseService.getLicenseUsingId(credentials,id)));
    }

    @PostMapping
    public ResponseEntity<License> createLicense(@RequestHeader(Credentials.AUTHORIZATION) String header, @Valid @RequestBody LicenseDto licenseDto){
        Credentials credentials = new Credentials(header);
        License license = licenseService.createLicense(licenseMapper.convertLicenseDtoToLicense(licenseDto),credentials);
        return ResponseEntity.created(URI.create("/license/"+license.getId())).body(license);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLicense(@RequestHeader(Credentials.AUTHORIZATION) String header, @PathVariable long id){
        Credentials credentials = new Credentials(header);
        licenseService.deleteLicense(id, credentials);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<License> updateLicense(@RequestHeader(Credentials.AUTHORIZATION) String header, @Valid @RequestBody License license, @PathVariable long id) {
        if (id != license.getId()) {
            throw new InvalidUpdateException();
        }
        Credentials credentials = new Credentials(header);
        return ResponseEntity.ok(licenseService.updateLicense(license, credentials));
    }


}
