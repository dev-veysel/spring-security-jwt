package com.hacker.service;


import com.hacker.dto.CompanyDTO;
import com.hacker.dto.response.CompanyResponse;
import com.hacker.dto.message.ResponseMessage;
import com.hacker.domain.Company;
import com.hacker.exception.ResourceNotFoundException;
import com.hacker.exception.message.ErrorMessage;
import com.hacker.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;


    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll()
                .stream().map(company -> {
                    CompanyDTO companyDTO = new CompanyDTO();
                    companyDTO.setCompanyName(company.getCompanyName());
                    companyDTO.setCompanyAddress(company.getCompanyAddress());
                    companyDTO.setCompanyPhoneNumber(company.getCompanyPhoneNumber());
                    companyDTO.setCompanyEmail(company.getCompanyEmail());
                    companyDTO.setNumberOfEmployee(company.getNumberOfEmployee());
                    companyDTO.setBusinessSector(company.getBusinessSector());
                    return companyDTO;
                }).toList();
    }

    public CompanyDTO getCompany(Integer id) {
        return companyRepository.findById(id)
                .map(company -> {
                    CompanyDTO companyDTO = new CompanyDTO();
                    companyDTO.setCompanyName(company.getCompanyName());
                    companyDTO.setCompanyAddress(company.getCompanyAddress());
                    companyDTO.setCompanyPhoneNumber(company.getCompanyPhoneNumber());
                    companyDTO.setCompanyEmail(company.getCompanyEmail());
                    companyDTO.setNumberOfEmployee(company.getNumberOfEmployee());
                    companyDTO.setBusinessSector(company.getBusinessSector());
                    return companyDTO;
                }).orElseThrow(
                        () -> new ResourceNotFoundException(
                                String.format(ErrorMessage.COMPANY_NOT_FOUND_EXCEPTION, id))
                );
    }

    public void createCompany(CompanyDTO companyDTO) {

        Company company = new Company();
        company.setCompanyName(companyDTO.getCompanyName());
        company.setCompanyAddress(companyDTO.getCompanyAddress());
        company.setCompanyPhoneNumber(companyDTO.getCompanyPhoneNumber());
        company.setCompanyEmail(companyDTO.getCompanyEmail());
        company.setNumberOfEmployee(companyDTO.getNumberOfEmployee());
        company.setBusinessSector(companyDTO.getBusinessSector());

        companyRepository.save(company);
    }


    public ResponseEntity<CompanyResponse> updateCompany(Integer id, CompanyDTO companyDTO) {
        Optional<Company> optionalCompany = companyRepository.findById(id);

        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();

            if (companyDTO.getCompanyName() != null) {
                company.setCompanyName(companyDTO.getCompanyName());
            }
            if (companyDTO.getCompanyAddress() != null) {
                company.setCompanyAddress(companyDTO.getCompanyAddress());
            }
            if (companyDTO.getCompanyPhoneNumber() != null) {
                company.setCompanyPhoneNumber(companyDTO.getCompanyPhoneNumber());
            }
            if (companyDTO.getCompanyEmail() != null) {
                company.setCompanyEmail(companyDTO.getCompanyEmail());
            }
            if (companyDTO.getNumberOfEmployee() != null) {
                company.setNumberOfEmployee(companyDTO.getNumberOfEmployee());
            }
            if (companyDTO.getBusinessSector() != null) {
                company.setBusinessSector(companyDTO.getBusinessSector());
            }
            companyRepository.save(company);

            CompanyResponse companyResponse = new CompanyResponse(ResponseMessage.UPDATE_COMPANY_SUCCESS, true);
            return new ResponseEntity<>(companyResponse, HttpStatus.OK);
        } else {
            CompanyResponse companyResponse = new CompanyResponse(ErrorMessage.COMPANY_NOT_FOUND_EXCEPTION, false);
            return new ResponseEntity<>(companyResponse, HttpStatus.NOT_FOUND);
        }
    }

    public void deleteCompany(Integer id) {
        companyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format(ErrorMessage.COMPANY_NOT_FOUND_EXCEPTION, id)));
        companyRepository.deleteById(id);
    }
}