package kh.edu.istad.platform.order.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@Table("order_address")
public class OderAddressEntity {
    @Id
    private UUID id;
    private String city;
    private String postalCode;
    private String street;

}
