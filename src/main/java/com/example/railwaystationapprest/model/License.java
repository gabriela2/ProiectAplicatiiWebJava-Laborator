package com.example.railwaystationapprest.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 20, unique = true)
    private String licenseNumber;
    @Column(nullable = false)
    private String licenseDescription;
    @ManyToMany(mappedBy = "licenses")
    private List<Company> companies;

    public License() {
    }

    public License(String licenseNumber, String licenseDescription) {
        this.licenseNumber = licenseNumber;
        this.licenseDescription = licenseDescription;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseDescription() {
        return licenseDescription;
    }

    public void setLicenseDescription(String licenseDescription) {
        this.licenseDescription = licenseDescription;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }
}
