package com.example.railwaystationapprest.service;

import com.example.railwaystationapprest.exception.BadCredentialsException;
import com.example.railwaystationapprest.exception.ObjectAlreadyExistsException;
import com.example.railwaystationapprest.exception.ObjectNotFoundException;
import com.example.railwaystationapprest.model.Address;
import com.example.railwaystationapprest.model.User;
import com.example.railwaystationapprest.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {
    @Mock
    private AddressRepository addressRepository;
    @InjectMocks
    private AddressService addressService;

    @Test
    void findByElements() {
        //arrange
        Address address = new Address("12A","Street","City","District","18348a");
        when(addressRepository.findByElements(address.getNumber(),address.getStreet(),address.getCity(),address.getDistrict(),address.getZipcode())).thenReturn(Optional.of(address));
        //act
        Optional<Address> result = addressService.findByElements(address.getNumber(),address.getStreet(),address.getCity(),address.getDistrict(),address.getZipcode());
        //assert
        assertNotNull(result);
        assertEquals(address.getNumber(),result.get().getNumber());
        assertEquals(address.getStreet(),result.get().getStreet());
        assertEquals(address.getCity(),result.get().getCity());
        assertEquals(address.getDistrict(),result.get().getDistrict());
        assertEquals(address.getZipcode(),result.get().getZipcode());
        verify(addressRepository).findByElements(address.getNumber(),address.getStreet(),address.getCity(),address.getDistrict(),address.getZipcode());
    }



    @Test
    @DisplayName("Credentiale incorecte")
    void createAddressIncorectCredentials() {
        //arrange

        User adminUser = new User();
        adminUser.setEmail("test@railway-transport.ro");
        adminUser.setPassword("test");
        Address address = new Address("12A","Street","City","District","18348a");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        //act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()->addressService.createAddress(address,credentials));
        //assert
        assertNotNull(exception);
        assertEquals("The credentials used are not correct",exception.getMessage());
        verify(addressRepository,times(0)).save(address);

    }


    @Test
    @DisplayName("Crediantiale corecte, obiect existent")
    void createAddressCorrectCredentialsObjectExists(){
        //arrange
        ReflectionTestUtils.setField(addressService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(addressService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Address address = new Address("12A","Street","City","District","18348a");
        when(addressRepository.findByElements(anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(Optional.of(address));
        //act
        ObjectAlreadyExistsException exception = assertThrows(ObjectAlreadyExistsException.class,()->addressService.createAddress(address,credentials));
        //assert
        assertEquals("Address already exists in the database",exception.getMessage());
        verify(addressRepository,times(0)).save(address);
    }

    @Test
    @DisplayName("Crediantiale corecte, obiectul nu exista")
    void createAddressCorrectCredentialsObjectDoesNotExist(){
        //arrange
        ReflectionTestUtils.setField(addressService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(addressService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Address address = new Address("12A","Street","City","District","18348a");
        when(addressRepository.findByElements(anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(Optional.empty());
        when(addressRepository.save(address)).thenReturn(address);
        //act

        Address result = addressService.createAddress(address,credentials);

        //assert
        assertEquals(address.getNumber(),result.getNumber());
        assertEquals(address.getStreet(),result.getStreet());
        assertEquals(address.getCity(),result.getCity());
        assertEquals(address.getDistrict(),result.getDistrict());
        assertEquals(address.getZipcode(),result.getZipcode());
        verify(addressRepository,times(1)).save(address);
    }



    @Test
    void deleteAddressIncorrectCredentials() {
        User adminUser = new User();
        adminUser.setEmail("test@railway-transport.ro");
        adminUser.setPassword("test");
        Address address = new Address("12A","Street","City","District","18348a");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        //act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()->addressService.deleteAddress(address,credentials));
        //assert
        assertNotNull(exception);
        assertEquals("The credentials used are not correct",exception.getMessage());
        verify(addressRepository,times(0)).save(address);
    }

    @Test
    @DisplayName("Crediantiale corecte, obiect existent")
    void deleteAddressCorrectCredentialsObjectExists(){
        //arrange
        ReflectionTestUtils.setField(addressService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(addressService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Address address = new Address("12A","Street","City","District","18348a");
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));

        //act
        addressService.deleteAddress(address,credentials);
        //assert
        verify(addressRepository,times(1)).delete(address);
    }

    @Test
    @DisplayName("Crediantiale corecte, obiect inexistent")
    void deleteAddressCorrectCredentialsObjectDoesNotExist(){
        //arrange
        ReflectionTestUtils.setField(addressService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(addressService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Address address = new Address("12A","Street","City","District","18348a");
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        //act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()->addressService.deleteAddress(address,credentials));
        //assert
        verify(addressRepository,times(0)).delete(address);
        assertEquals("Address does not exist",exception.getMessage());
    }

    @Test
    void update() {
        User adminUser = new User();
        adminUser.setEmail("test@railway-transport.ro");
        adminUser.setPassword("test");
        Address address = new Address("12A","Street","City","District","18348a");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        //act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,()->addressService.update(address,credentials));
        //assert
        assertNotNull(exception);
        assertEquals("The credentials used are not correct",exception.getMessage());
        verify(addressRepository,times(0)).save(address);
    }

    @Test
    @DisplayName("Crediantiale corecte, obiect inexistent")
    void updateAddressCorrectCredentialsObjectDoesNotExist(){
        //arrange
        ReflectionTestUtils.setField(addressService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(addressService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Address address = new Address("12A","Street","City","District","18348a");
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        //act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,()->addressService.deleteAddress(address,credentials));
        //assert
        verify(addressRepository,times(0)).save(address);
        assertEquals("Address does not exist",exception.getMessage());
    }

    @Test
    @DisplayName("Crediantiale corecte, obiect existent")
    void updateAddressCorrectCredentialsObjectExists(){
        //arrange
        ReflectionTestUtils.setField(addressService,"adminEmail","admin@railway-transport.ro");
        ReflectionTestUtils.setField(addressService,"adminPassword","admin");
        User adminUser = new User();
        adminUser.setEmail("admin@railway-transport.ro");
        adminUser.setPassword("admin");
        Credentials credentials = new Credentials();
        credentials.setEmail(adminUser.getEmail());
        credentials.setPassword(adminUser.getPassword());
        Address address = new Address("12A","Street","City","District","18348a");
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));
        when(addressRepository.save(address)).thenReturn(address);

        //act
        Address result = addressService.update(address,credentials);
        //assert
        verify(addressRepository,times(1)).save(address);
        assertEquals(address.getNumber(),result.getNumber());
    }
}