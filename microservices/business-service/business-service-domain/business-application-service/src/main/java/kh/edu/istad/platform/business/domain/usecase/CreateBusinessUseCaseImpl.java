package kh.edu.istad.platform.business.domain.usecase;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.ProductId;
import kh.edu.istad.platform.business.domain.dto.CreateBusinessCommand;
import kh.edu.istad.platform.business.domain.dto.CreateBusinessResponse;
import kh.edu.istad.platform.business.domain.entity.Business;
import kh.edu.istad.platform.business.domain.entity.Product;
import kh.edu.istad.platform.business.domain.port.out.BusinessRepository;
import kh.edu.istad.platform.business.domain.valueobject.BusinessContact;
import kh.edu.istad.platform.business.domain.valueobject.BusinessName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateBusinessUseCaseImpl implements CreateBusinessUseCase {

    private final BusinessRepository businessRepository;

    @Override
    public Mono<CreateBusinessResponse> execute(CreateBusinessCommand command) {
        log.info("Executing create business usecase for: {}", command.legalName());

        return Mono.fromCallable(() -> {
                    List<Product> products = command.products().stream()
                            .map(dto -> Product.builder()
                                    .id(new ProductId(dto.id() != null ? dto.id() : UUID.randomUUID()))
                                    .name(dto.name())
                                    .price(new Money(dto.price()))
                                    .quantity(dto.quantity() != null ? dto.quantity() : 0)
                                    .available(dto.available() != null ? dto.available() : true)
                                    .build())
                            .toList();

                    Business business = Business.builder()
                            .businessName(new BusinessName(command.legalName(), command.displayName()))
                            .businessContact(new BusinessContact(command.email(), command.phone(), command.address()))
                            .products(products)
                            .build();

                    // Execute core domain rules
                    business.initializeBusiness();
                    business.validateBusiness();

                    return business;
                })
                .flatMap(businessRepository::save)
                .map(saved -> new CreateBusinessResponse(
                        saved.getId().value(),
                        "Business created successfully"
                ));
    }
}