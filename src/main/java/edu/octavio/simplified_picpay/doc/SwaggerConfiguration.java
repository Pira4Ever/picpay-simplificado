package edu.octavio.simplified_picpay.doc;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfiguration {
    @Bean
    public OpenAPI springShopOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info().title("Simplified PicPay")
                        .description("A simplified version of PicPay's backend")
                        .version("v1.0")
                        .license(new License().name("MIT").url("https://github.com/Pira4Ever/picpay-simplificado/blob/main/LICENSE"))
                        .contact(new Contact().name("Octavio Piratininga").url("https://github.com/Pira4Ever").email("59701790+Pira4Ever@users.noreply.github.com"))
                        .termsOfService("Terms of use: Open Source"));
    }

}
