package com.example.railwaystationapprest.repository;

import com.example.railwaystationapprest.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LicenseRepository extends JpaRepository<License,Long> {
    Optional<License> findByLicenseNumber(String licenseNumber);
}
