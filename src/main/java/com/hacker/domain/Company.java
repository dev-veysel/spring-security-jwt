package com.hacker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/* **********************      Welcome to Hackers Security - API     **************************** */

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "_company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    @Setter(AccessLevel.NONE)
    private Integer id;

    @Size(min = 3, max = 40, message = "Your company name '${validatedValue}' must be between {min} and {max} letters long! \n Please try again.")
    @NotNull(message = "Please enter your company name")
    @Column(length = 40, nullable = false)
    private String companyName;

    @NotNull(message = "Please enter your company address")
    private String companyAddress;

    @NotNull(message = "Please enter your company phone number")
    private String companyPhoneNumber;

    @Column(unique = true)
    @Email(message = "Please enter a valid email")
    private String companyEmail;

    @NotNull(message = "Please enter your number of employee")
    private Integer numberOfEmployee;

    @NotNull(message = "Please enter your business sector")
    private String businessSector;

    @OneToMany(mappedBy = "company")
    private List<ServiceOffer> offers = new ArrayList<>();

}