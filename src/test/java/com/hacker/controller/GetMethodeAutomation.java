package com.hacker.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GetMethodeAutomation {

    public static String baseURI = "http://localhost:8080/api/v1/companies";


    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = baseURI; // Die Basis-URL Ihres Spring Boot-Servers
    }

    @Test
    public void testGetAllCompanies() {
        get("/visitors/all")
                .then()
                .statusCode(200) // Überprüfen, ob der Statuscode 200 (OK) ist
                .contentType(ContentType.JSON) // Überprüfen, ob die Antwort als JSON vorliegt
                .body("size()", greaterThan(0)); // Überprüfen, ob die Antwort mindestens ein Element enthält
    }

    @Test
    public void testGetCompany() {
        int companyIdToTest = 1; // Ersetzen Sie dies durch die gewünschte ID für Ihren Test

        get("/visitors/" + companyIdToTest)
                .then()
                .statusCode(200) // Überprüfen, ob der Statuscode 200 (OK) ist
                .contentType(ContentType.JSON) // Überprüfen, ob die Antwort als JSON vorliegt
                .body("id", equalTo(companyIdToTest)); // Überprüfen, ob die Antwort die erwartete ID enthält
    }


}