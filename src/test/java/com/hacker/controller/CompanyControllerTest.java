package com.hacker.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacker.domain.Company;
import com.hacker.dto.CompanyDTO;
import com.hacker.dto.response.CompanyResponse;
import com.hacker.repository.CompanyRepository;
import com.hacker.service.CompanyService;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {CompanyController.class})
@ExtendWith(SpringExtension.class)
class CompanyControllerTest {
    @Autowired
    private CompanyController companyController;

    @MockBean
    private CompanyService companyService;




    @Test
    void testGetAllCompanies() throws Exception {
        when(companyService.getAllCompanies()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/companies/visitors/all");
        MockMvcBuilders.standaloneSetup(companyController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    void testGetCompany() throws Exception {
        when(companyService.getCompany(Mockito.<Integer>any())).thenReturn(new CompanyDTO());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/companies/visitors/{id}", 1);
        MockMvcBuilders.standaloneSetup(companyController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"companyName\":null,\"companyAddress\":null,\"companyPhoneNumber\":null,\"companyEmail\":null,"
                                        + "\"numberOfEmployee\":null,\"businessSector\":null}"));
    }


    @Test
    void testCreateCompany() throws Exception {
        doNothing().when(companyService).createCompany(Mockito.<CompanyDTO>any());

        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setBusinessSector("Business Sector");
        companyDTO.setCompanyAddress("42 Main St");
        companyDTO.setCompanyEmail("jane.doe@example.org");
        companyDTO.setCompanyName("Company Name");
        companyDTO.setCompanyPhoneNumber("6625550144");
        companyDTO.setId(1);
        companyDTO.setNumberOfEmployee(10);
        String content = (new ObjectMapper()).writeValueAsString(companyDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/companies/visitors/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(companyController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"message\":\"Company was successfully created\",\"success\":true}"));
    }


    @Test
    void testUpdateCompany() {
        Company company = new Company();
        company.setBusinessSector("Business Sector");
        company.setCompanyAddress("42 Main St");
        company.setCompanyEmail("jane.doe@example.org");
        company.setCompanyName("Company Name");
        company.setCompanyPhoneNumber("6625550144");
        company.setNumberOfEmployee(10);
        company.setOffers(new ArrayList<>());
        Optional<Company> ofResult = Optional.of(company);

        Company company2 = new Company();
        company2.setBusinessSector("Business Sector");
        company2.setCompanyAddress("42 Main St");
        company2.setCompanyEmail("jane.doe@example.org");
        company2.setCompanyName("Company Name");
        company2.setCompanyPhoneNumber("6625550144");
        company2.setNumberOfEmployee(10);
        company2.setOffers(new ArrayList<>());
        CompanyRepository companyRepository = mock(CompanyRepository.class);
        when(companyRepository.save(Mockito.<Company>any())).thenReturn(company2);
        when(companyRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        CompanyController companyController = new CompanyController(new CompanyService(companyRepository));
        ResponseEntity<CompanyResponse> actualUpdateCompanyResult = companyController.updateCompany(1, new CompanyDTO());
        assertTrue(actualUpdateCompanyResult.hasBody());
        assertTrue(actualUpdateCompanyResult.getHeaders().isEmpty());
        assertEquals(200, actualUpdateCompanyResult.getStatusCodeValue());
        CompanyResponse body = actualUpdateCompanyResult.getBody();
        assertTrue(body.isSuccess());
        assertEquals("Company was successfully updated", body.getMessage());
        verify(companyRepository).save(Mockito.<Company>any());
        verify(companyRepository).findById(Mockito.<Integer>any());
    }



    @Test
    void testDeleteCompany() throws Exception {
        doNothing().when(companyService).deleteCompany(Mockito.<Integer>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/companies/admin/auth/delete/{id}", 1);
        MockMvcBuilders.standaloneSetup(companyController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"message\":\"Company was successfully deleted\",\"success\":true}"));
    }
}