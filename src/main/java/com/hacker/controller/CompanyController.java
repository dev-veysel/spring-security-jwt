package com.hacker.controller;

import com.hacker.dto.CompanyDTO;
import com.hacker.dto.response.CompanyResponse;
import com.hacker.dto.message.ResponseMessage;
import com.hacker.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // Get all Companies
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/allCompanies")
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        List<CompanyDTO> companyDTOList = companyService.getAllCompanies();
        return ResponseEntity.ok(companyDTOList);
    }


    // Get one Company
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}") //http://localhost:8080/api/v1/companies/1
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable ("id") Integer id) {
        return new ResponseEntity<>(companyService.getCompany(id), HttpStatus.OK);
    }


    // Create new Company
    @PreAuthorize("hasAnyRole('ADMIN') or hasAnyRole('USER')")
    @PostMapping("/createCompany") //localhost:8080/api/v1/companies/createCompany
    public ResponseEntity<CompanyResponse> createCompany(@RequestBody CompanyDTO companyDTO) {
        companyService.createCompany(companyDTO);

        CompanyResponse companyResponse = new CompanyResponse(ResponseMessage.CREATE_COMPANY_SUCCESS, true);
        return new ResponseEntity<>(companyResponse, HttpStatus.CREATED);
    }


    // Update existing Company
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(@PathVariable Integer id,
                                                         @Valid @RequestBody CompanyDTO companyDTO) {
        companyService.updateCompany(id, companyDTO);

        CompanyResponse companyResponse = new CompanyResponse(ResponseMessage.UPDATE_COMPANY_SUCCESS, true);
        return new ResponseEntity<>(companyResponse, HttpStatus.OK);
    }


    // Delete one Company
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CompanyResponse> deleteCompany(@PathVariable Integer id) {
        companyService.deleteCompany(id);

        CompanyResponse companyResponse = new CompanyResponse(ResponseMessage.DELETE_COMPANY_SUCCESS, true);
        return new ResponseEntity<>(companyResponse, HttpStatus.OK);
    }
}