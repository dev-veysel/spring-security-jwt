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
public class UserUpdateRequest {

    @Size(max=50)
    @NotBlank(message="Please provide your First name")
    private String firstName;

    @Size(max=50)
    @NotBlank(message="Please provide your Last name")
    private String lastName;

    @Size(min=5  , max=80)
    @Email(message="Please provide your email")
    private String email;

    @Size(max=50)
    @NotBlank(message="Please provide your Username")
    private String username;

}