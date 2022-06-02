package com.example.railwaystationapprest.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(length = 15, nullable = false, unique = true)
    private String identificationNumber;
    @OneToMany(mappedBy = "company")
    private List<Journey> journeys;
    @ManyToMany
    @JoinTable(name ="companies_licenses", joinColumns = @JoinColumn(name = "company_id",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name = "license_id", referencedColumnName = "id"))
    private List<License> licenses;

    public Company() {
    }

    public Company(String name, String identificationNumber) {
        this.name = name;
        this.identificationNumber = identificationNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public List<Journey> getJourneys() {
        return journeys;
    }

    public void setJourneys(List<Journey> journeys) {
        this.journeys = journeys;
    }

    public List<License> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<License> licenses) {
        this.licenses = licenses;
    }

    public void addLicense(License license){
        licenses.add(license);
    }

    public void removeLicense(License license){
        licenses.remove(license);
    }
}
