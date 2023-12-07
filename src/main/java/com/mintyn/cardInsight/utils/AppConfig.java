package com.mintyn.cardInsight.utils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "springdoc.swagger-ui.enabled", havingValue = "true", matchIfMissing = true)
public class AppConfig {
    @Value("${api.info.licence.name: Licence 2.0}")
    private String licenceName;
    @Value("${api.info.licence.url: https://www.apache.org/licenses/LICENSE-2.0}")
    private String licenceUrl;

    @Bean
    public OpenAPI productApi() {
        return new OpenAPI()
                .info(getApiInfo());
    }
    private Info getApiInfo() {
        Contact contact = new Contact().name("Olukunle Afolabi").email("afolabikunle2@gmail.com").url("https://github.com/Thinkit-lab");
        License licence = new License().name(licenceName).url(licenceUrl);
        return new Info()
                .title("Mintyn Assessment")
                .description("Complete REST Card Insight API consumable with web clients")
                .version("V1")
                .contact(contact)
                .license(licence);
    }

}
