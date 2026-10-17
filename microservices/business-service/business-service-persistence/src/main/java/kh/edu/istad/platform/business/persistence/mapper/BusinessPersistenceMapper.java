package kh.edu.istad.platform.business.persistence.mapper;

import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.ProductId;
import kh.edu.istad.platform.business.domain.entity.Business;
import kh.edu.istad.platform.business.domain.entity.Product;
import kh.edu.istad.platform.business.domain.valueobject.BusinessContact;
import kh.edu.istad.platform.business.domain.valueobject.BusinessName;
import kh.edu.istad.platform.business.persistence.document.BusinessMongoDocument;
import kh.edu.istad.platform.business.persistence.document.ProductMongoSubDocument;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BusinessPersistenceMapper {

    public BusinessMongoDocument businessToDocument(Business business) {
        List<ProductMongoSubDocument> productDocs = business.getProducts() != null
                ? business.getProducts().stream()
                .map(p -> ProductMongoSubDocument.builder()
                        .id(p.getId().value())
                        .name(p.getName())
                        .price(p.getPrice().amount())
                        .quantity(p.getQuantity())
                        .available(p.isAvailable())
                        .build())
                .toList()
                : new ArrayList<>();

        return BusinessMongoDocument.builder()
                .id(business.getId().value())
                .legalName(business.getBusinessName().legalName())
                .displayName(business.getBusinessName().displayName())
                .email(business.getBusinessContact().email())
                .phone(business.getBusinessContact().phone())
                .address(business.getBusinessContact().address())
                .active(business.getActive())
                .products(productDocs)
                .build();
    }

    public Business documentToBusiness(BusinessMongoDocument doc) {
        List<Product> products = doc.getProducts() != null
                ? doc.getProducts().stream()
                .map(d -> Product.builder()
                        .id(new ProductId(d.getId()))
                        .name(d.getName())
                        .price(new Money(d.getPrice()))
                        .quantity(d.getQuantity())
                        .available(d.getAvailable())
                        .build())
                .toList()
                : new ArrayList<>();

        return Business.builder()
                .id(new BusinessId(doc.getId()))
                .businessName(new BusinessName(doc.getLegalName(), doc.getDisplayName()))
                .businessContact(new BusinessContact(doc.getEmail(), doc.getPhone(), doc.getAddress()))
                .active(doc.getActive())
                .products(products)
                .build();
    }
}