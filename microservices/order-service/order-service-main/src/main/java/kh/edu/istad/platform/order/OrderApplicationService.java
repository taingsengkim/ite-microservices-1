package kh.edu.istad.platform.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "kh.edu.istad"
})
public class OrderApplicationService {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplicationService.class, args);
    }
}