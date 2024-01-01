package com.hacker.controller;

import com.hacker.dto.CompanyDTO;
import com.hacker.dto.response.CompanyResponse;
import com.hacker.dto.message.ResponseMessage;
import com.hacker.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/companies")
@Tag(name = "CRUD REST APIs for Company Controller") //für Swagger
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // Get all Companies
    @GetMapping("/visitors/all")
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        List<CompanyDTO> companyDTOList = companyService.getAllCompanies();
        return ResponseEntity.ok(companyDTOList);
    }



    // Get one Company
    @GetMapping("/visitors/{id}")
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable ("id") Integer id) {
        return new ResponseEntity<>(companyService.getCompany(id), HttpStatus.OK);
    }


    // Create new Company
    @PostMapping("/visitors/create")
    public ResponseEntity<CompanyResponse> createCompany(@RequestBody CompanyDTO companyDTO) {
        companyService.createCompany(companyDTO);

        CompanyResponse companyResponse = new CompanyResponse(ResponseMessage.CREATE_COMPANY_SUCCESS, true);
        return new ResponseEntity<>(companyResponse, HttpStatus.CREATED);
    }


    // Update existing Company
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update existing Company API", description = "Update Put REST API is used to update company into database")
    @ApiResponse(responseCode = "200", description = "Http Status 200 CREATED")

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/auth/update/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(@PathVariable Integer id,
                                                         @Valid @RequestBody CompanyDTO companyDTO) {
        companyService.updateCompany(id, companyDTO);

        CompanyResponse companyResponse = new CompanyResponse(ResponseMessage.UPDATE_COMPANY_SUCCESS, true);
        return new ResponseEntity<>(companyResponse, HttpStatus.OK);
    }


    // Delete one Company
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/auth/delete/{id}")
    public ResponseEntity<CompanyResponse> deleteCompany(@PathVariable Integer id) {
        companyService.deleteCompany(id);

        CompanyResponse companyResponse = new CompanyResponse(ResponseMessage.DELETE_COMPANY_SUCCESS, true);
        return new ResponseEntity<>(companyResponse, HttpStatus.OK);
    }
}