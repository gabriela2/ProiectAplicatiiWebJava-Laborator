package com.example.railwaystationapprest.service;

import com.example.railwaystationapprest.exception.BadCredentialsException;
import com.example.railwaystationapprest.exception.ObjectAlreadyExistsException;
import com.example.railwaystationapprest.exception.ObjectCannotBeDeletedException;
import com.example.railwaystationapprest.exception.ObjectNotFoundException;
import com.example.railwaystationapprest.model.Address;
import com.example.railwaystationapprest.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {
    private AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }
    @Value("${administrator.email}")
    private String adminEmail;
    @Value("${administrator.password}")
    private String adminPassword;

    public void checkCredentials(Credentials credentials) {
        if(!(credentials.getEmail().equals(adminEmail) && credentials.getPassword().equals(adminPassword))){
            throw new BadCredentialsException();
        }
    }

    public Optional<Address> findByElements(String number, String street, String city, String district, String zipcode){
        return addressRepository.findByElements(number,street,city,district,zipcode);
    }

    public Address createAddress(Address address, Credentials credentials){
        checkCredentials(credentials);
        Optional<Address> existingAddress = addressRepository.findByElements(address.getNumber(),address.getStreet(),address.getCity(),address.getDistrict(),address.getZipcode());
        if(existingAddress.isPresent()){
            throw new ObjectAlreadyExistsException("Address");
        }
        return addressRepository.save(address);
    }

    public void deleteAddress(Address address, Credentials credentials) {
        checkCredentials(credentials);
        Optional<Address> existingAddress = addressRepository.findById(address.getId());
        if (existingAddress.isPresent()){
            addressRepository.delete(address);
        }else {
            throw new ObjectNotFoundException("Address");
        }
    }

    public Address update(Address address, Credentials credentials) {
        checkCredentials(credentials);
        addressRepository.findById(address.getId()).orElseThrow(()-> new ObjectNotFoundException("Address"));
        return addressRepository.save(address);
    }
}
