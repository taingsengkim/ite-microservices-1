package kh.edu.istad.platform.customer.domain.entity;


import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.CustomerSegmentId;
import kh.edu.istad.platform.customer.domain.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;

import java.util.UUID;

public class Customer extends AggregateRoot<CustomerId> {
    private final String username;
    private String familyName;
    private String givenName;
    private CustomerSegmentId customerSegmentId;

    public String getUsername() {
        return username;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public Email getEmail() {
        return email;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public CustomerSegmentId getCustomerSegmentId() {
        return customerSegmentId;
    }


    private final Email email;
    private final PhoneNumber phoneNumber;
    private CustomerStatus status;

    // Domain Critical Logic
    public void initiateCustomer() {
        validateCustomer();

        super.setId(new CustomerId(UUID.randomUUID()));
        status = CustomerStatus.ACTIVE;
    }

    public void updateCustomer(String familyName, String givenName){
        if(familyName == null || givenName==null){
            throw new CustomerDomainException(("Family name and given name must not be null"));
        }

        this.familyName = familyName;
        this.givenName = givenName;
    }

    public void deactiveCustomer(){
        if(status != CustomerStatus.ACTIVE){
            throw new CustomerDomainException("Could not deactive, this customer might be already inactive");
        }
        status = CustomerStatus.INACTIVE;
    }


    private void validateCustomer() {
        if (customerSegmentId == null) {
            throw new CustomerDomainException(
                    "Customer segment must not be null"
            );
        }
        if (getId() != null) {
            throw new CustomerDomainException(
                    "Customer already exists!"
            );
        }

        if (status != null) {
            throw new CustomerDomainException(
                    "Customer status must be null before initialization"
            );
        }

        if (username == null || username.isBlank()) {
            throw new CustomerDomainException(
                    "Username must not be blank"
            );
        }

        if (email == null || phoneNumber == null) {
            throw new CustomerDomainException(
                    "Email and phone number must not be null"
            );
        }
    }



    private Customer(Builder builder) {
        super.setId(builder.id);
        customerSegmentId = builder.customerSegmentId;
        username = builder.username;
        familyName = builder.familyName;
        givenName = builder.givenName;
        email = builder.email;
        phoneNumber = builder.phoneNumber;
        status = builder.status;
    }

    public static final class Builder {
        private CustomerId id;
        private CustomerSegmentId customerSegmentId;
        private String username;
        private String familyName;
        private String givenName;
        private Email email;
        private PhoneNumber phoneNumber;
        private CustomerStatus status;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(CustomerId val) {
            id = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder familyName(String val) {
            familyName = val;
            return this;
        }

        public Builder givenName(String val) {
            givenName = val;
            return this;
        }

        public Builder email(Email val) {
            email = val;
            return this;
        }

        public Builder phoneNumber(PhoneNumber val) {
            phoneNumber = val;
            return this;
        }
        public Builder customerSegmentId(CustomerSegmentId val) {
            customerSegmentId = val;
            return this;
        }

        public Builder status(CustomerStatus val) {
            status = val;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
