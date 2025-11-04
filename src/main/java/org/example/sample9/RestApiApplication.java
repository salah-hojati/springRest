package org.example.sample9;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync; // Import this

@SpringBootApplication
@EnableAsync // Add this annotation
@OpenAPIDefinition(
    info = @Info(
        title = "Bookstore API",
        version = "1.0.0",
        description = "A simple API for managing a collection of books."
    )
)
public class RestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApiApplication.class, args);
    }
}
