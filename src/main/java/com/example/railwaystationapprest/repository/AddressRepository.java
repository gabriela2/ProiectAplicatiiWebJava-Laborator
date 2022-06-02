package com.example.railwaystationapprest.repository;

import com.example.railwaystationapprest.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query(nativeQuery = true, value = "select * from address where number =:number and street=:street and city=:city and district=:district and zipcode=:zipcode")
    Optional<Address> findByElements(String number, String street, String city, String district, String zipcode);

}
