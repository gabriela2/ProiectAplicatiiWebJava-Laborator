package com.example.railwaystationapprest.service;

import com.example.railwaystationapprest.exception.BadCredentialsException;
import com.example.railwaystationapprest.exception.ObjectAlreadyExistsException;
import com.example.railwaystationapprest.exception.ObjectCannotBeUpdatedException;
import com.example.railwaystationapprest.exception.ObjectNotFoundException;
import com.example.railwaystationapprest.model.License;
import com.example.railwaystationapprest.model.User;
import com.example.railwaystationapprest.repository.LicenseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LicenseServiceTest {

    @Mock
    LicenseRepository licenseRepository;
    @InjectMocks
    LicenseService licenseService;

    @Test
    void getLicenseById() {
        License license = new License();
        license.setLicenseNumber("abc");
        when(licenseRepository.findById(anyLong())).thenReturn(Optional.of(license));
        Optional<License> result = licenseService.getLicenseById(1l);
        assertEquals(license.getLicenseNumber(),result.get().getLicenseNumber());
    }

    @Test
    void getLicensesCredentialsNotOk() {
        User adminUser = new User();
        adminUser.setEmail("test@railway-transport.ro");
        adminUser.setPassword("test");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        //act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()->licenseService.getLicenses(credentials));
        //assert
        assertNotNull(exception);
        assertEquals("The credentials used are not correct",exception.getMessage());
    }

    @Test
    void getLicensesCredentialsOk() {
        ReflectionTestUtils.setField(licenseService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(licenseService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        List<License> licenses = new ArrayList<>();
        when(licenseRepository.findAll()).thenReturn(licenses);
        //act
        List<License> result =licenseService.getLicenses(credentials);
        //assert
        assertEquals(licenses.size(),result.size());
    }

    @Test
    void getLicenseUsingIdNotFound() {
        ReflectionTestUtils.setField(licenseService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(licenseService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        License license = new License();
        when(licenseRepository.findById(anyLong())).thenReturn(Optional.empty());
        //act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()->licenseService.getLicenseUsingId(credentials,1l));
        //assert
        assertEquals("License does not exist",exception.getMessage());
    }

    @Test
    void getLicenseUsingIdFound() {
        ReflectionTestUtils.setField(licenseService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(licenseService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        License license = new License();
        license.setLicenseNumber("abc");
        when(licenseRepository.findById(anyLong())).thenReturn(Optional.of(license));
        //act
        License result = licenseService.getLicenseUsingId(credentials,1l);
        //assert
        assertEquals(license.getLicenseNumber(),result.getLicenseNumber());
    }

    @Test
    void createLicenseExista() {
        ReflectionTestUtils.setField(licenseService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(licenseService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        License license = new License();
        license.setLicenseNumber("abc");
        when(licenseRepository.findByLicenseNumber("abc")).thenReturn(Optional.of(license));
        //act
        ObjectAlreadyExistsException exception = assertThrows(ObjectAlreadyExistsException.class,()->licenseService.createLicense(license,credentials));
        //assert
        assertEquals("License already exists in the database",exception.getMessage());
    }

    @Test
    void createLicenseNuExista() {
        ReflectionTestUtils.setField(licenseService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(licenseService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        License license = new License();
        license.setLicenseNumber("abc");
        when(licenseRepository.findByLicenseNumber("abc")).thenReturn(Optional.empty());
        when(licenseRepository.save(license)).thenReturn(license);
        //act
        License result = licenseService.createLicense(license,credentials);
        //assert
        assertEquals(license.getLicenseNumber(),result.getLicenseNumber());
    }

    @Test
    void deleteLicenseIdNotFound() {
        ReflectionTestUtils.setField(licenseService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(licenseService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        License license = new License();
        license.setLicenseNumber("abc");
        when(licenseRepository.findById(1l)).thenReturn(Optional.empty());
        //act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()->licenseService.deleteLicense(1l,credentials));
        //assert
        assertEquals("License does not exist",exception.getMessage());
    }

    @Test
    void deleteLicenseIdFound() {
        ReflectionTestUtils.setField(licenseService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(licenseService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        License license = new License();
        license.setLicenseNumber("abc");
        when(licenseRepository.findById(1l)).thenReturn(Optional.of(license));
        //act
        licenseService.deleteLicense(1l,credentials);
        //assert
        verify(licenseRepository,times(1)).delete(license);
    }

    @Test
    void updateLicenseIdNotFound(){
        ReflectionTestUtils.setField(licenseService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(licenseService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        License license = new License();
        license.setId(1l);
        license.setLicenseNumber("abc");
        when(licenseRepository.findById(1l)).thenReturn(Optional.empty());
        //act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()->licenseService.updateLicense(license,credentials));
        //assert
        assertEquals("License does not exist",exception.getMessage());
    }

    @Test
    void updateLicenseIdFoundNotOK(){
        ReflectionTestUtils.setField(licenseService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(licenseService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        License license = new License();
        license.setId(1l);
        license.setLicenseNumber("abc");
        License license1 = new License();
        license1.setId(2);
        license1.setLicenseNumber("cba");
        when(licenseRepository.findById(1l)).thenReturn(Optional.of(license1));
        //act
        ObjectCannotBeUpdatedException exception = assertThrows(ObjectCannotBeUpdatedException.class,()->licenseService.updateLicense(license,credentials));
        //assert
        assertEquals("The License cannot be changed",exception.getMessage());
    }

    @Test
    void updateLicenseIdFoundOK(){
        ReflectionTestUtils.setField(licenseService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(licenseService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        License license = new License();
        license.setId(1l);
        license.setLicenseNumber("abc");

        when(licenseRepository.findById(1l)).thenReturn(Optional.of(license));
        when(licenseRepository.save(license)).thenReturn(license);
        //act
        License result = licenseService.updateLicense(license,credentials);
        //assert
        assertEquals(license.getLicenseNumber(),result.getLicenseNumber());
    }

}