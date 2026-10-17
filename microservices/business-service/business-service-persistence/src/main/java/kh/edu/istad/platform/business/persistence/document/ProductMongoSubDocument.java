package kh.edu.istad.platform.business.persistence.document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductMongoSubDocument {
    private UUID id;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private Boolean available;
}