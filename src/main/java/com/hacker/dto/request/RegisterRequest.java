package com.hacker.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Size(max=50)
    @NotBlank(message="Please provide your first name")
    private String firstName;

    @Size(max=50)
    @NotBlank(message="Please provide your last name")
    private String lastName;

    @Size(min=5  , max=80)
    @Email(message="Please provide your email")
    private String email;

    @Size(max=50)
    @NotBlank(message="Please provide your Username")
    private String username;

    @Size(min=4 , max=20, message="Please provide Correct Size of Password")
    @NotBlank(message="Please provide your password")
    private String password;

}
