package com.works;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories(basePackages = "com.works.repositories._elastic")
@SpringBootApplication
public class CompanyAllProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompanyAllProjectApplication.class, args);
    }

}
