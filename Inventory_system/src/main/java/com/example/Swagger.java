package com.example;



import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "Product Management",
                version = "1.0",
                description = "REST APIs for managing products",
                contact = @Contact(
                        name = "Narmadha",
                        email = "narmadha27@gmail.com",
                        url = "https://github.com/hariprasanth-jcode"
                ),
                license = @License(
                        name = "MIT License"
                )
        )
)   

public class Swagger {

}
