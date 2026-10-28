package kh.edu.istad.platform.customer.client.bean;

import kh.edu.istad.platform.customer.client.domain.service.CustomerDomainService;
import kh.edu.istad.platform.customer.client.domain.service.CustomerDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public CustomerDomainService customerDomainService(){
        return new CustomerDomainServiceImpl();
    }
}
