package com.banking.platform.card.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Banking Platform Card APIs",
                description = "Banking Platform Card APIs management",
                contact = @Contact(
                        name = "Collins Rashid",
                        email = "rashidcollins16@gmail.com"
                ),
                version = "v1"
        ),

        servers = {
                @Server(
                        description = "DEV",
                        url = "http://localhost:8083"
                ),
        }
)
public class DocumentationConfig {
}
