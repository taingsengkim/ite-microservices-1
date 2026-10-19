
package kh.edu.istad.platform.customer.persistence.mapper;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import kh.edu.istad.platform.customer.persistence.entity.CustomerDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerPersistenceMapper {

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "email.value", target = "email")
    @Mapping(source = "phoneNumber.value", target = "phoneNumber")
    @Mapping(source = "status", target = "customerStatus")
    CustomerDocument toDocument(Customer customer);

    default Customer toDomain(CustomerDocument document) {
        if (document == null) {
            return null;
        }

        return Customer.Builder.builder()
                .id(stringToCustomerId(document.getId()))
                .username(document.getUsername())
                .familyName(document.getFamilyName())
                .givenName(document.getGivenName())
                .email(stringToEmail(document.getEmail()))
                .phoneNumber(stringToPhoneNumber(document.getPhoneNumber()))
                .status(document.getCustomerStatus())
                .build();
    }

    default CustomerId stringToCustomerId(String id) {
        return id == null ? null : new CustomerId(UUID.fromString(id));
    }

    default String emailToString(Email email) {
        return email == null ? null : email.value();
    }

    default Email stringToEmail(String email) {
        return email == null ? null : new Email(email);
    }

    default String phoneNumberToString(PhoneNumber phoneNumber) {
        return phoneNumber == null ? null : phoneNumber.value();
    }

    default PhoneNumber stringToPhoneNumber(String phoneNumber) {
        return phoneNumber == null ? null : new PhoneNumber(phoneNumber);
    }
}
