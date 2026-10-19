package kh.edu.istad.platform.customer.restapi.mapper;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.dto.InitiatedCustomerResult;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateResponse;
import kh.edu.istad.platform.customer.restapi.dto.CustomerUpdateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerUpdateResponse;
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
                .build();
    }

    @Mapping(target = "customerId", source = "id")
    CustomerInitiateResponse toCustomerInitiateResponse(Customer customer);

    CustomerInitiateResponse toInitiatedResponse(InitiatedCustomerResult result);

    UpdateCustomerCommand toUpdateCustomerCommand(CustomerUpdateRequest request);

    CustomerUpdateResponse toCustomerUpdateResponse(UpdateCustomerResult result);
    default CustomerId toCustomerId(UUID id) {
        return id == null ? null : new CustomerId(id);
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