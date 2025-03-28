package com.programming.seekho.accounts;

import com.programming.seekho.accounts.dto.AccountsContactInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties(value = AccountsContactInfoDto.class)
@OpenAPIDefinition(
		info = @Info(
				title = "Accounts microservices REST API Documentation",
				description = "PS Bank Accounts microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Anuj Kumar",
						email = "graphicsbuddy2024@gmail.com",
						url = "https://programmingseekho.in"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://programmingseekho.in"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "PS Bank Accounts microservice REST API Documentation",
				url = "https://programmingseekho.in/swagger-ui.html"
		)
)
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
		System.out.println("Accounts application running");
	}

}
