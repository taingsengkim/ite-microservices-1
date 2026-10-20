package kh.edu.istad.platform.customer.restapi.mapper;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.CustomerSegmentId;
import kh.edu.istad.platform.customer.domain.dto.CustomerWithSegmentType;
import kh.edu.istad.platform.customer.domain.dto.InitiatedCustomerResult;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.entity.CustomerSegment;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import kh.edu.istad.platform.customer.restapi.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CustomerWebMapper {

    default Customer toCustomer(CustomerInitiateRequest request) {
        if (request == null) {
            return null;
        }

        return Customer.Builder.builder()
                .username(request.username())
                .familyName(request.familyName())
                .givenName(request.givenName())
                .email(stringToEmail(request.email()))
                .phoneNumber(stringToPhoneNumber(request.phoneNumber()))
                .customerSegmentId(
                        toCustomerSegmentId(request.customerSegmentId())
                )
                .build();
    }


    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "username", source = "customer.username")
    @Mapping(target = "familyName", source = "customer.familyName")
    @Mapping(target = "givenName", source = "customer.givenName")
    @Mapping(target = "email", source = "customer.email")
    @Mapping(target = "phoneNumber", source = "customer.phoneNumber")
    @Mapping(target = "customerStatus", source = "customer.status")
    @Mapping(target = "customerSegmentType", source = "customerSegmentType")
    CustomerResponse toCustomerResponse(CustomerWithSegmentType result);

    CustomerSegment toCustomerSegment(CustomerSegmentRequest segmentRequest);


    @Mapping(target = "customerId", source = "id")
    CustomerInitiateResponse toCustomerInitiateResponse(Customer customer);

    @Mapping(target = "id", source = "id")
    CustomerSegmentResponse toCustomerSegmentResponse(CustomerSegment customer);

    CustomerInitiateResponse toInitiatedResponse(InitiatedCustomerResult result);


    @Mapping(target = "familyName", source = "familyName")
    @Mapping(target = "givenName", source = "givenName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "customerSegmentId", source = "customerSegmentId")
    UpdateCustomerCommand toUpdateCustomerCommand(CustomerUpdateRequest request);


    @Mapping(target = "customerId", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "familyName", source = "familyName")
    @Mapping(target = "givenName", source = "givenName")
    @Mapping(target = "email", expression = "java(customer.getEmail().value())")
    @Mapping(target = "phoneNumber", expression = "java(customer.getPhoneNumber().value())")
    @Mapping(target = "customerStatus", expression = "java(customer.getStatus().name())")
    @Mapping(target = "customerSegmentType", ignore = true)
    CustomerResponse toCustomerResponse(Customer customer);


    CustomerUpdateResponse toCustomerUpdateResponse(UpdateCustomerResult result);
    default CustomerSegmentId toCustomerSegmentId(UUID id) {
        return id == null ? null : new CustomerSegmentId(id);
    }
    default CustomerId toCustomerId(UUID id) {
        return id == null ? null : new CustomerId(id);
    }
    default UUID customerSegmentIdToUuid(CustomerSegmentId id) {
        return id == null ? null : id.value();
    }

    default UUID customerIdToUuid(CustomerId id) {
        return id == null ? null : id.value();
    }
    default Email stringToEmail(String email) {
        return email == null ? null : new Email(email);
    }
    default String emailToString(Email email) {
        return email == null ? null : email.value();
    }
    default PhoneNumber stringToPhoneNumber(String phone) {
        return phone == null ? null : new PhoneNumber(phone);
    }
    default String phoneNumberToString(PhoneNumber phone) {
        return phone == null ? null : phone.value();
    }
}