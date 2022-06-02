package com.example.railwaystationapprest.repository;

import com.example.railwaystationapprest.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByIdentificationNumber(String identificationNumber);

    Optional<Company> findByName(String companyName);
}
