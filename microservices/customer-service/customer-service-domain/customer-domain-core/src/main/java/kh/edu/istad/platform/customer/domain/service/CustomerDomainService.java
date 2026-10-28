package kh.edu.istad.platform.customer.client.domain.service;

import kh.edu.istad.platform.customer.client.domain.entity.Customer;
import kh.edu.istad.platform.customer.client.domain.event.CustomerDeactivatedEvent;
import kh.edu.istad.platform.customer.client.domain.event.CustomerInitiatedEvent;
import kh.edu.istad.platform.customer.client.domain.event.CustomerUpdatedEvent;

public interface CustomerDomainService {
    CustomerInitiatedEvent initiateCustomer(Customer customer);

    CustomerUpdatedEvent updateCustomer(Customer customer);

    CustomerDeactivatedEvent deactivateCustomer(Customer customer);


}
