package com.hacker.security;

import com.hacker.security.jwt.*;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.*;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer")
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //CSRF disable wird nicht empfohlen wegen Cyberattacke!
        // csrf wird disable, da wir security schreiben und "update" Verfahren nicht Seitens
        // Security stattfinden soll! Wir arbeiten mit RestFull Verfahren!
        http
                .csrf(csrfCustomize -> csrfCustomize.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/login",
                        "/register",
                        "/loginPage",
                        "/home",
                        "/api/v1/companies/visitors/**")
                .permitAll()

                .requestMatchers("/swagger-ui/**")
                .permitAll()
                .requestMatchers("/v3/api-docs")
                .permitAll()

                .anyRequest().authenticated()

                .and()
                .formLogin(form ->
                        form
                                .loginPage("/loginPage")
                                .loginProcessingUrl("/home")
                                //.loginProcessingUrl("/authenticateTheUser")
                                //.successForwardUrl("/home")
                                .permitAll()
                )

                .logout().permitAll()
        ;


        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    //*************** CORS ****************************

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source  = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:63342"));
        config.setAllowedMethods(List.of("*"));
        config.setMaxAge(Duration.ofMinutes(10));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter();

    }


    //*******************SWAGGER***********************

//    private static final String [] AUTH_WHITE_LIST= {
//            "/v3/api-docs/**", // swagger
//            "swagger-ui.html", //swagger
//            "/swagger-ui/**", // swagger
//            "/",
//            "index.html",
//            "/images/**",
//            "/css/**",
//            "/js/**"
//    };

//    // yukardaki static listeyi de giriş izni veriyoruz, boiler plate
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        WebSecurityCustomizer customizer=new WebSecurityCustomizer() {
//            @Override
//            public void customize(WebSecurity web) {
//                web.ignoring().requestMatchers(AUTH_WHITE_LIST);
//            }
//        };
//        return customizer;
//    }



    //*************** PASSWORD ENCODER ****************************
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10); //4-31-- Nicht >15 sonst viel Speicher
    }



    //*************** PROVIDER ****************************
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }


    //*************** AUTHENTICATION MANAGER ****************************
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).
                authenticationProvider(authProvider()).
                build();
    }


    //*************** AUTH TOKEN FILTER ****************************
    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

}