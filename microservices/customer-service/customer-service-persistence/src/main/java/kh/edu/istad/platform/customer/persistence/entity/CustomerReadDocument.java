package kh.edu.istad.platform.customer.persistence.entity;

import com.mongodb.DBRef;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Document(collection = "customers")
public class CustomerReadDocument {

    @Id
    private String id;

    private String username;
    private String familyName;
    private String givenName;
    private String email;
    private String phoneNumber;
    private CustomerStatus customerStatus;
    private DBRef customerSegmentDocument;
    private String createdBy;
    private Instant createdAt;
    private String modifiedBy;
    private Instant modifiedAt;
}