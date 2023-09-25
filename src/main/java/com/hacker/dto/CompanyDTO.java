package com.hacker.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {

    private Integer id;

    private String companyName;

    private String companyAddress;

    private String companyPhoneNumber;

    private String companyEmail;

    private Integer numberOfEmployee;

    private String businessSector;

}