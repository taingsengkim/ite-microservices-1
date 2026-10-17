package kh.edu.istad.platform.business.restapi.mapper;

import kh.edu.istad.platform.business.domain.dto.CreateBusinessCommand;
import kh.edu.istad.platform.business.domain.dto.CreateBusinessResponse;
import kh.edu.istad.platform.business.restapi.dto.CreateBusinessRequest;
import kh.edu.istad.platform.business.restapi.dto.CreateBusinessWebResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BusinessRestMapper {

    public CreateBusinessCommand toCommand(CreateBusinessRequest request) {
        List<CreateBusinessCommand.ProductDto> products = request.products() != null
                ? request.products().stream()
                .map(p -> new CreateBusinessCommand.ProductDto(
                        p.id(),
                        p.name(),
                        p.price(),
                        p.quantity(),
                        p.available()
                ))
                .toList()
                : new ArrayList<>();

        return new CreateBusinessCommand(
                request.legalName(),
                request.displayName(),
                request.email(),
                request.phone(),
                request.address(),
                products
        );
    }

    public CreateBusinessWebResponse toWebResponse(CreateBusinessResponse response) {
        return new CreateBusinessWebResponse(
                response.businessId(),
                response.message()
        );
    }
}