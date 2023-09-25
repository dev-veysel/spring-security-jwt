package com.hacker.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserUpdateRequest {

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

    @Size(min=4, max=20, message="Please provide correct size of password")
    @NotBlank(message="Please provide your password")
    private String password;

    private Set<String> roles;
}