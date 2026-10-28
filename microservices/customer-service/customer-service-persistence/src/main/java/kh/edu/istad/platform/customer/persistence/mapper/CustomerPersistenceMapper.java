
package kh.edu.istad.platform.customer.client.persistence.mapper;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.CustomerSegmentId;
import kh.edu.istad.platform.customer.client.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.client.domain.entity.Customer;
import kh.edu.istad.platform.customer.client.domain.valueobject.Email;
import kh.edu.istad.platform.customer.client.domain.valueobject.PhoneNumber;
import kh.edu.istad.platform.customer.client.persistence.entity.CustomerDocument;
import kh.edu.istad.platform.customer.client.persistence.entity.CustomerReadDocument;
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

    CustomerDocument fromUpdateCustomerCommandToDocument(UpdateCustomerCommand updateCustomerCommand);

    default Customer toDomain(CustomerReadDocument document) {
        if (document == null) {
            return null;
        }

        CustomerSegmentId segmentId = null;

        if (document.getCustomerSegmentDocument() != null
                && document.getCustomerSegmentDocument().getId() != null) {
            segmentId = new CustomerSegmentId(
                    UUID.fromString(
                            document.getCustomerSegmentDocument().getId().toString()
                    )
            );
        }

        return Customer.Builder.builder()
                .id(stringToCustomerId(document.getId()))
                .username(document.getUsername())
                .familyName(document.getFamilyName())
                .givenName(document.getGivenName())
                .email(stringToEmail(document.getEmail()))
                .phoneNumber(stringToPhoneNumber(document.getPhoneNumber()))
                .status(document.getCustomerStatus())
                .customerSegmentId(segmentId)
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
