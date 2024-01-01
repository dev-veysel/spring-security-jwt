package com.hacker.openapi;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(         info = @Info(
                            title = "Hackers Security API",
                            description = "Hackers Security App API Documentation",
                            version = "v1.0",
                            contact = @Contact(
                                    name = "Veysel",
                                    email = "veysel@gmail.com",
                                    url = "..."),
                            license = @License(
                                    name = "Apache 2.0",
                                    url = "...")
                            ),

                            externalDocs = @ExternalDocumentation(
                                    description = "Hackers Security App Documentation",
                                    url = "github.com/repository"
                            ),

                            security = @SecurityRequirement(
                                    name = "Bearer")
)
///Swagger: http://localhost:8080/v3/api-docs
public class OpenApiConfig {
}
