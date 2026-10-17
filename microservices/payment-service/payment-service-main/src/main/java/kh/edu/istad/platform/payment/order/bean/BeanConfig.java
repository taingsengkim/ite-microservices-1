package kh.edu.istad.platform.payment.order.bean;



import kh.edu.istad.platform.payment.order.service.PaymentDomainService;
import kh.edu.istad.platform.payment.order.service.PaymentDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public PaymentDomainService paymentDomainService() {
        return new PaymentDomainServiceImpl();
    }
}
