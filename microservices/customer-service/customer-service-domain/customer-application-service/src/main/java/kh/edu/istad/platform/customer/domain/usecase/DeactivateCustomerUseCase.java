package kh.edu.istad.platform.customer.domain.usecase;


import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeactivateCustomerUseCase {

    private final CustomerRepository customerRepository;

    public void execute(CustomerId id){
        Customer customer = customerRepository.findById(id).orElseThrow(()->new CustomerDomainException("Customer Not Found : " + id));
        customer.deactiveCustomer();
        customerRepository.save(customer);
        log.info("Customer dactivated : {}",id);
    }
}
