package com.hacker.controller;


import com.hacker.dto.response.CompanyResponse;
import com.hacker.dto.message.ResponseMessage;
import com.hacker.dto.request.LoginRequest;
import com.hacker.dto.request.RegisterRequest;
import com.hacker.dto.response.LoginResponse;
import com.hacker.security.jwt.JwtUtils;
import com.hacker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping
public class UserJwtController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public ResponseEntity<CompanyResponse> registerUser(@Valid
                 @RequestBody RegisterRequest registerRequest) {
        userService.saveUser(registerRequest);

        CompanyResponse response = new CompanyResponse();
        response.setMessage(ResponseMessage.REGISTER_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid
                           @RequestBody LoginRequest loginRequest) {

        //die Login Informationen werden hier vom Benutzer in Token umgewandelt
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                                                            loginRequest.getPassword());

             Authentication authentication =
                  authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            // !!! Kullanıcı bu aşamada valide edildi ve Token üretimine geçiliyor
             UserDetails userDetails = (UserDetails) authentication.getPrincipal(); //getPrincipal -> Current Login olan kullancinin bilgilerni UserDetails türünden getiriyor!
             String jwtToken = jwtUtils.generateJwtToken(userDetails);

             // !!! JWT token client tarafına gönderiliyor
            LoginResponse loginResponse = new LoginResponse(jwtToken);

            return new ResponseEntity<>(loginResponse,HttpStatus.OK);
    }


}
