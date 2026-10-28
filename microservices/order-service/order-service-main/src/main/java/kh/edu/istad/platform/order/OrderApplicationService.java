package kh.edu.istad.platform.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages = "kh.edu.istad")
//@EnableReactiveMongoRepositories(basePackages = "kh.edu.istad.platform.order.persistence.repository")
public class OrderApplicationService {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplicationService.class, args);
    }

}
