package com.example.railwaystationapprest.service;

import com.example.railwaystationapprest.exception.*;
import com.example.railwaystationapprest.model.Address;
import com.example.railwaystationapprest.model.RailwayStation;
import com.example.railwaystationapprest.repository.RailwayStationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RailwayStationService {
    private RailwayStationRepository railwayStationRepository;
    private AddressService addressService;

    public RailwayStationService(RailwayStationRepository railwayStationRepository, AddressService addressService) {
        this.railwayStationRepository = railwayStationRepository;
        this.addressService = addressService;
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

    public RailwayStation getRailwayStationByName(String railwayStation) {
        return railwayStationRepository.getByName(railwayStation).orElseThrow(()-> new ObjectNotFoundException("Railway station"));
    }

    @Transactional
    public RailwayStation createRailwayStation(RailwayStation railwayStation, Credentials credentials){
        checkCredentials(credentials);
        Optional<RailwayStation> existingRailwayStation = railwayStationRepository.getByName(railwayStation.getName());
        if(existingRailwayStation.isPresent()){
            throw new ObjectAlreadyExistsException("RailwayStation");
        }
        Address address = new Address(railwayStation.getAddress().getNumber(),railwayStation.getAddress().getStreet(),railwayStation.getAddress().getCity(),railwayStation.getAddress().getDistrict(),railwayStation.getAddress().getZipcode());
        Optional<Address> existingAddress = addressService.findByElements(address.getNumber(),address.getStreet(),address.getCity(),address.getDistrict(),address.getZipcode());
        if(existingAddress.isPresent()){
            throw new ObjectAlreadyExistsException("Address");
        }
        Address savedAddress = addressService.createAddress(address,credentials);
        railwayStation.setAddress(savedAddress);
        return railwayStationRepository.save(railwayStation);
    }

    @Transactional
    public void deleteRailwayStation(long id, Credentials credentials){
        checkCredentials(credentials);
        RailwayStation exitingRailwayStation = railwayStationRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Railway Station"));
        if(exitingRailwayStation.getArrivalJourneys().isEmpty() && exitingRailwayStation.getDepartureJourneys().isEmpty()){
            addressService.deleteAddress(exitingRailwayStation.getAddress(),credentials);
            railwayStationRepository.delete(exitingRailwayStation);
        }else {
            throw new ObjectCannotBeDeletedException("Railway Station");
        }
    }

    @Transactional
    public RailwayStation update(long id,RailwayStation railwayStation, Credentials credentials) {
        checkCredentials(credentials);
        RailwayStation existingRailwayStation = railwayStationRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Railway Station"));
        if(existingRailwayStation.getArrivalJourneys().isEmpty() && existingRailwayStation.getDepartureJourneys().isEmpty()){
            railwayStation.getAddress().setId(existingRailwayStation.getAddress().getId());
            addressService.update(railwayStation.getAddress(),credentials);
            railwayStation.setId(id);
            return railwayStationRepository.save(railwayStation);
        }else {
            throw new ObjectCannotBeUpdatedException("Railway Station");

        }
    }

    public List<RailwayStation> getRailwayStations(String sort,String addressComponent,String address) {
        if(addressComponent == null){
            if(sort==null){return railwayStationRepository.findAll();}
            if(sort.equals("asc")){return railwayStationRepository.findAll().stream().sorted(Comparator.comparing(RailwayStation::getName)).collect(Collectors.toList());}
            if(sort.equals("desc")){return railwayStationRepository.findAll().stream().sorted(Comparator.comparing(RailwayStation::getName).reversed()).collect(Collectors.toList());}
        }else if(addressComponent.equals("city")){
            if(address != null){
                if(sort==null){return railwayStationRepository.findAllByCity(address);}
                if(sort.equals("asc")){return railwayStationRepository.findAllByCity(address).stream().sorted(Comparator.comparing(RailwayStation::getName)).collect(Collectors.toList());}
                if(sort.equals("desc")){return railwayStationRepository.findAllByCity(address).stream().sorted(Comparator.comparing(RailwayStation::getName).reversed()).collect(Collectors.toList());}
            }
        }else if(addressComponent.equals("district")){
            if(address != null){
                if(sort==null){return railwayStationRepository.findAllByDistrict(address);}
                if(sort.equals("asc")){return railwayStationRepository.findAllByDistrict(address).stream().sorted(Comparator.comparing(RailwayStation::getName)).collect(Collectors.toList());}
                if(sort.equals("desc")){return railwayStationRepository.findAllByDistrict(address).stream().sorted(Comparator.comparing(RailwayStation::getName).reversed()).collect(Collectors.toList());}
            }
        }
        return railwayStationRepository.findAll();
    }

    public RailwayStation getRailwayStationById(long id) {
        RailwayStation exitingRailwayStation = railwayStationRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Railway Station"));
        return exitingRailwayStation;
    }

}
