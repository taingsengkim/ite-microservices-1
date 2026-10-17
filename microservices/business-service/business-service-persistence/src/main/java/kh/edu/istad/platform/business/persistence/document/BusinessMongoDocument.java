package kh.edu.istad.platform.business.persistence.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "businesses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessMongoDocument {

    @Id
    private UUID id;
    private String legalName;
    private String displayName;
    private String email;
    private String phone;
    private String address;
    private Boolean active;
    private List<ProductMongoSubDocument> products;
}