package com.hacker;

import com.hacker.controller.CompanyController;
import com.hacker.dto.CompanyDTO;
import com.hacker.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CompanyControllerTest {

    private CompanyController companyController;

    @Mock
    private CompanyService companyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        companyController = new CompanyController(companyService);
    }

    @Test
    void testGetAllCompanies() {
        // Erstelle eine Beispiel-Liste von CompanyDTOs, die der Service zurückgeben würde
        // GIVEN
        List<CompanyDTO> sampleCompanyDTOList = new ArrayList<>();
        // Füge hier beliebige CompanyDTO-Objekte hinzu

        // Stelle sicher, dass der CompanyService das erwartete Verhalten aufweist
        // WHEN
        when(companyService.getAllCompanies()).thenReturn(sampleCompanyDTOList);

        // Rufe die Methode des Controllers auf
        ResponseEntity<List<CompanyDTO>> responseEntity = companyController.getAllCompanies();

        // Überprüfe, ob die Methode ResponseEntity.ok() zurückgibt
        // THEN
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Überprüfe, ob die zurückgegebene Liste von CompanyDTOs dieselbe ist wie die Beispielliste
        assertEquals(sampleCompanyDTOList, responseEntity.getBody());

        // Überprüfe, ob der CompanyService-Methode getAllCompanies genau einmal aufgerufen wurde
        verify(companyService, times(1)).getAllCompanies();
    }
}