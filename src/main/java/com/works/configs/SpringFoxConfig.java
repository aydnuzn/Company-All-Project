package com.works.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    @Bean
    public Docket api() {

        SecurityReference securityReference = SecurityReference.builder()
                .reference("basicAuth")
                .scopes(new AuthorizationScope[0])
                .build();

        ArrayList<SecurityReference> reference = new ArrayList<>(1);
        reference.add(securityReference);

        ArrayList<SecurityContext> securityContexts = new ArrayList<>(1);
        securityContexts.add(SecurityContext.builder().securityReferences(reference).build());

        ArrayList<SecurityScheme> auth = new ArrayList<>(1);
        auth.add(new BasicAuth("basicAuth"));

        return new Docket(DocumentationType.SWAGGER_2)
                .securitySchemes(auth)
                .securityContexts(securityContexts)
                .select()
                .paths(PathSelectors.regex("(/rest/admin)|(/rest/forgotpassword/.*)"))
                .build()
                .apiInfo(apiInfo());
    }

    //Swagger ana sayfasında alt bilgi
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Company All Project - Rest Api",
                "Admin-Account -> selen@mail.com, 123456",
                "API 1.0",
                "https://github.com/selenkosoglu/Company-All-Project",
                new Contact("Project GitHub", "https://github.com/selenkosoglu/Company-All-Project", "mertdumanli.cse@gmail.com"),
                "License of API", "https://github.com/selenkosoglu/Company-All-Project", Collections.emptyList());
    }
}