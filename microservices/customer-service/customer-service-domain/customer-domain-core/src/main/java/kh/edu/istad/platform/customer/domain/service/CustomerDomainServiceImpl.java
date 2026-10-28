package kh.edu.istad.platform.customer.client.domain.service;

import kh.edu.istad.platform.customer.client.domain.entity.Customer;
import kh.edu.istad.platform.customer.client.domain.event.CustomerDeactivatedEvent;
import kh.edu.istad.platform.customer.client.domain.event.CustomerInitiatedEvent;
import kh.edu.istad.platform.customer.client.domain.event.CustomerUpdatedEvent;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CustomerDomainServiceImpl implements CustomerDomainService{
    @Override
    public CustomerInitiatedEvent initiateCustomer(Customer customer) {
        customer.initiateCustomer();
        return new CustomerInitiatedEvent(customer, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public CustomerUpdatedEvent updateCustomer(Customer customer) {
        customer.updateCustomer(customer.getFamilyName(),customer.getGivenName());
        return new CustomerUpdatedEvent(customer, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public CustomerDeactivatedEvent deactivateCustomer(Customer customer) {
        customer.deactiveCustomer();
        return new CustomerDeactivatedEvent(customer, ZonedDateTime.now(ZoneId.of("UTC")));
    }

}
