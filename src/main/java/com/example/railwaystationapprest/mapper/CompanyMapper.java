package com.example.railwaystationapprest.mapper;

import com.example.railwaystationapprest.dto.CompanyDto;
import com.example.railwaystationapprest.model.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {
    public Company convertCompanyDtoToCompany(CompanyDto companyDto){
        return new Company(companyDto.getName(),companyDto.getIdentificationNumber());
    }
    public CompanyDto convertCompanyToCompanyDto(Company company){
        return new CompanyDto(company.getName(),company.getIdentificationNumber());
    }
}
