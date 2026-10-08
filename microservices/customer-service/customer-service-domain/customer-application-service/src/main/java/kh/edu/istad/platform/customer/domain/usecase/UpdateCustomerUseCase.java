package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.domain.mapper.CustomerDomainMapper;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateCustomerUseCase {
    private final CustomerRepository customerRepository;
    private final CustomerDomainMapper customerDomainMapper;

    public UpdateCustomerResult execute(CustomerId id, UpdateCustomerCommand updateCustomerCommand){
        Customer customer = customerRepository.findById(id).orElseThrow(()->new CustomerDomainException("Customer not found with this id"));
        customer.updateCustomer(
                updateCustomerCommand.familyName(),
                updateCustomerCommand.givenName()
        );
        customerRepository.save(customer);
        return customerDomainMapper.fromCustomerToUpdateCustomerResult(customer);
    }
}
