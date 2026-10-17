package kh.edu.istad.platform.business.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication(scanBasePackages = "kh.edu.istad.platform.business")
@EnableReactiveMongoRepositories(basePackages = "kh.edu.istad.platform.business.persistence.repository")
public class BusinessServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinessServiceApplication.class, args);
    }
}