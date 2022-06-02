package com.example.railwaystationapprest.service;

import com.example.railwaystationapprest.exception.*;
import com.example.railwaystationapprest.model.*;
import com.example.railwaystationapprest.repository.CompanyRepository;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {
    @Mock
    CompanyRepository companyRepository;
    @Mock
    JourneyService journeyService;
    @Mock
    LicenseService licenseService;

    @InjectMocks
    CompanyService companyService;


    @Test
    @DisplayName("Credentiale not ok")
    void createCompanyCredentialeNotOk() {
        //arrange
        User adminUser = new User();
        adminUser.setEmail("test@railway-transport.ro");
        adminUser.setPassword("test");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company();
        //act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()->companyService.createCompany(company,credentials));
        //assert
        assertNotNull(exception);
        assertEquals("The credentials used are not correct",exception.getMessage());
        verify(companyRepository,times(0)).save(company);
    }

    @Test
    @DisplayName("Credentiale ok, companie exista")
    void createCompanyCredentialeOkCompanieExista() {
        //arrange
        ReflectionTestUtils.setField(companyService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(companyService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company("38,due","uhs3883");
        when(companyRepository.findByIdentificationNumber(anyString())).thenReturn(Optional.of(company));
        //act
        ObjectAlreadyExistsException exception = assertThrows(ObjectAlreadyExistsException.class,()->companyService.createCompany(company,credentials));
        //assert
        assertEquals("Company already exists in the database",exception.getMessage());
        verify(companyRepository,times(0)).save(company);
    }

    @Test
    @DisplayName("Credentiale ok, compania nu exista")
    void createCompanyCredentialeOkCompaniaNuExista() {
        //arrange
        ReflectionTestUtils.setField(companyService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(companyService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company("38,due","uhs3883");
        when(companyRepository.findByIdentificationNumber(anyString())).thenReturn(Optional.empty());
        when(companyRepository.save(company)).thenReturn(company);
        //act
        Company result = companyService.createCompany(company,credentials);
        //assert
        assertEquals(company.getIdentificationNumber(),result.getIdentificationNumber());
        assertEquals(company.getName(),result.getName());
        verify(companyRepository,times(1)).save(company);
    }

    @Test
    @DisplayName("Credentiale not ok")
    void deleteCompanyCredentialeNotOk() {
        //arrange
        User adminUser = new User();
        adminUser.setEmail("test@railway-transport.ro");
        adminUser.setPassword("test");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company();
        //act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()->companyService.createCompany(company,credentials));
        //assert
        assertNotNull(exception);
        assertEquals("The credentials used are not correct",exception.getMessage());
        verify(companyRepository,times(0)).save(company);
    }

    @Test
    @DisplayName("Credentiale  ok")
    void deleteCompaniesCredentialeOkIdNotFound() {
        //arrange
        ReflectionTestUtils.setField(companyService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(companyService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company("38,due","uhs3883");
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());
        //act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()->companyService.deleteCompany(1,credentials));
        //assert
        assertEquals("Company does not exist",exception.getMessage());
        verify(companyRepository,times(0)).deleteById(anyLong());
    }

    @Test
    void deleteCompaniesCredentialeOkIdOkJourneyEmpty() {
        //arrange
        ReflectionTestUtils.setField(companyService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(companyService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company("38,due","uhs3883");
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));
        List<Journey> journeys = new ArrayList<>();
        when(journeyService.getJourneysByCompanyId(anyLong())).thenReturn(journeys);
        //act
        companyService.deleteCompany(anyLong(),credentials);
        //assert
        verify(companyRepository,times(1)).deleteById(anyLong());
    }

    @Test
    void deleteCompaniesCredentialeOkIdOkJourneyNotEmpty() {
        //arrange
        ReflectionTestUtils.setField(companyService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(companyService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company("38,due","uhs3883");
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));
        List<Journey> journeys = new ArrayList<>();
        Journey journey = new Journey();
        journeys.add(journey);
        when(journeyService.getJourneysByCompanyId(anyLong())).thenReturn(journeys);
        //act
        ObjectCannotBeDeletedException exception = assertThrows(ObjectCannotBeDeletedException.class,()->companyService.deleteCompany(anyLong(),credentials));
        //assert
        verify(companyRepository,times(0)).deleteById(anyLong());
        assertEquals("Company cannot be deleted",exception.getMessage());
    }


    @Test
    @DisplayName("Credentiale not ok")
    void getCompaniesCredentialeNotOk() {
        //arrange
        User adminUser = new User();
        adminUser.setEmail("test@railway-transport.ro");
        adminUser.setPassword("test");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company();
        //act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()->companyService.getCompanies(credentials));
        //assert
        assertNotNull(exception);
        assertEquals("The credentials used are not correct",exception.getMessage());
        verify(companyRepository,times(0)).findAll();
    }

    @Test
    @DisplayName("Credentiale not ok")
    void getCompaniesCredentialeOk() {
        //arrange
        ReflectionTestUtils.setField(companyService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(companyService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company("38,due","uhs3883");
        List<Company> companies = new ArrayList<>();
        companies.add(company);
        when(companyRepository.findAll()).thenReturn(companies);
        //act
        List<Company> result = companyService.getCompanies(credentials);
        //assert
        assertEquals(companies.size(),result.size());
        verify(companyRepository,times(1)).findAll();
    }

    @Test
    void getCompanyByIdCredentialeNotOk() {
        //arrange
        User adminUser = new User();
        adminUser.setEmail("test@railway-transport.ro");
        adminUser.setPassword("test");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company();
        //act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()->companyService.getCompanyById(credentials,1));
        //assert
        assertNotNull(exception);
        assertEquals("The credentials used are not correct",exception.getMessage());
        verify(companyRepository,times(0)).findAll();
    }

    @Test
    @DisplayName("Credentiale ok, id nu exista")
    void getCompanyByIdCredentialeOk() {
        //arrange
        ReflectionTestUtils.setField(companyService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(companyService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company("38,due","uhs3883");
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());
        //act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()->companyService.getCompanyById(credentials,anyLong()));
        //assert
        assertEquals("Company does not exist",exception.getMessage());

    }

    @Test
    @DisplayName("Credentiale ok, id ul exista")
    void getCompanyByIdCredentialeOkIdOk() {
        //arrange
        ReflectionTestUtils.setField(companyService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(companyService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company("38,due","uhs3883");
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));
        //act
        Company result = companyService.getCompanyById(credentials,anyLong());
        //assert
        assertEquals(company.getName(),result.getName());

    }

    @Test
    void createCompanyLicenseCredentialeNotOk() {
        //arrange
        User adminUser = new User();
        adminUser.setEmail("test@railway-transport.ro");
        adminUser.setPassword("test");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company();
        //act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()->companyService.createCompanyLicense(credentials,1,1));
        //assert
        assertNotNull(exception);
        assertEquals("The credentials used are not correct",exception.getMessage());
        verify(companyRepository,times(0)).findAll();
    }

    @Test
    void createCompanyLicenseCredentialeOkIdCompanyNotOk() {
        //arrange
        ReflectionTestUtils.setField(companyService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(companyService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company("38,due","uhs3883");
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());
        //act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()->companyService.createCompanyLicense(credentials,1,1));
        //assert
        assertEquals("Company does not exist",exception.getMessage());

    }

    @Test
    void createCompanyLicenseCredentialeOkIdCompanyOkIdLicenseNotOk() {
        //arrange
        ReflectionTestUtils.setField(companyService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(companyService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company("38,due","uhs3883");
        License license = new License("AJA","AJSJ");
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));
        when(licenseService.getLicenseById(anyLong())).thenReturn(Optional.empty());
        //act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()->companyService.createCompanyLicense(credentials,1,1));
        //assert
        assertEquals("License does not exist",exception.getMessage());

    }

    @Test
    void createCompanyLicenseCredentialeOkIdCompanyOkIdLicenseOkLinked() {
        //arrange
        ReflectionTestUtils.setField(companyService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(companyService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company("38,due","uhs3883");
        License license = new License("AJA","AJSJ");
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));
        when(licenseService.getLicenseById(anyLong())).thenReturn(Optional.of(license));
        List<License> licenses = new ArrayList<>();
        licenses.add(license);
        company.setLicenses(licenses);
        //act
        ObjectCannotBeCreatedException exception = assertThrows(ObjectCannotBeCreatedException.class,()->companyService.createCompanyLicense(credentials,1,1));
        //assert
        assertEquals("License - Company cannot be created",exception.getMessage());

    }

    @Test
    void createCompanyLicenseCredentialeOkIdCompanyOkIdLicenseOk() {
        //arrange
        ReflectionTestUtils.setField(companyService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(companyService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company("38,due","uhs3883");
        License license = new License("AJA","AJSJ");
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));
        when(licenseService.getLicenseById(anyLong())).thenReturn(Optional.of(license));
        List<License> licenses = new ArrayList<>();
        company.setLicenses(licenses);
        //act
        companyService.createCompanyLicense(credentials,1,1);
        //assert
        verify(companyRepository,times(1)).save(company);

    }






    @Test
    void deleteCompanyLicenseCredentialeNotOk() {
        //arrange
        User adminUser = new User();
        adminUser.setEmail("test@railway-transport.ro");
        adminUser.setPassword("test");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company();
        //act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()->companyService.deleteCompanyLicense(credentials,1,1));
        //assert
        assertNotNull(exception);
        assertEquals("The credentials used are not correct",exception.getMessage());
        verify(companyRepository,times(0)).findAll();
    }

    @Test
    void deleteCompanyLicenseCredentialeOkIdCompanyNotOk() {
        //arrange
        ReflectionTestUtils.setField(companyService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(companyService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company("38,due","uhs3883");
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());
        //act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()->companyService.deleteCompanyLicense(credentials,1,1));
        //assert
        assertEquals("Company does not exist",exception.getMessage());

    }

    @Test
    void deleteCompanyLicenseCredentialeOkIdCompanyOkIdLicenseNotOk() {
        //arrange
        ReflectionTestUtils.setField(companyService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(companyService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company("38,due","uhs3883");
        License license = new License("AJA","AJSJ");
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));
        when(licenseService.getLicenseById(anyLong())).thenReturn(Optional.empty());
        //act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()->companyService.deleteCompanyLicense(credentials,1,1));
        //assert
        assertEquals("License does not exist",exception.getMessage());

    }

    @Test
    void deleteCompanyLicenseCredentialeOkIdCompanyOkIdLicenseOkLinked() {
        //arrange
        ReflectionTestUtils.setField(companyService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(companyService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company("38,due","uhs3883");
        License license = new License("AJA","AJSJ");
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));
        when(licenseService.getLicenseById(anyLong())).thenReturn(Optional.of(license));
        List<License> licenses = new ArrayList<>();
        licenses.add(license);
        company.setLicenses(licenses);
        //act
        companyService.deleteCompanyLicense(credentials,1,1);
        //assert
        verify(companyRepository,times(1)).save(company);
    }

    @Test
    void createCompanyLicenseCredentialeOkIdCompanyOkIdLicenseNotLinked() {
        //arrange
        ReflectionTestUtils.setField(companyService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(companyService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Company company = new Company("38,due","uhs3883");
        License license = new License("AJA","AJSJ");
        company.setLicenses(new ArrayList<>());
        company.getLicenses().add(new License());
        when(companyRepository.findById(1l)).thenReturn(Optional.of(company));
        when(licenseService.getLicenseById(1l)).thenReturn(Optional.of(license));
        //act
        ObjectCannotBeDeletedException exception = assertThrows(ObjectCannotBeDeletedException.class,()->companyService.deleteCompanyLicense(credentials,1,1));
        //assert
        assertEquals("Company-License cannot be deleted",exception.getMessage());

    }




    @Test
    @DisplayName("Compania  exista")
    void getCompanyByNameOk() {
        //arrange
        Company company = new Company("ABC","123");
        when(companyRepository.findByName(anyString())).thenReturn(Optional.of(company));
        //act
        Company result = companyService.getCompanyByName(anyString());
        //assert
        assertEquals(company.getName(),result.getName());

    }
    @Test
    @DisplayName("Compania nu exista")
    void getCompanyByNameNotOK() {
        //arrange
        Company company = new Company("ABC","123");
        when(companyRepository.findByName(anyString())).thenReturn(Optional.empty());
        //act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()-> companyService.getCompanyByName(anyString()));
        //assert
        assertEquals("Company does not exist",exception.getMessage());

    }
}