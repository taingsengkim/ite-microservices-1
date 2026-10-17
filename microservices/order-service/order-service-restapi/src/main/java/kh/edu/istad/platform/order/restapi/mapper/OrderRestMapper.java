package kh.edu.istad.platform.order.restapi.mapper;

import kh.edu.istad.platform.order.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.dto.CreateOrderResponse;
import kh.edu.istad.platform.order.restapi.dto.CreateOrderRequest;
import kh.edu.istad.platform.order.restapi.dto.CreateOrderWebResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderRestMapper {

    public CreateOrderCommand toCommand(CreateOrderRequest request) {
        CreateOrderCommand.OrderAddressDto addressDto = new CreateOrderCommand.OrderAddressDto(
                request.address().street(),
                request.address().postalCode(),
                request.address().city()
        );

        List<CreateOrderCommand.OrderItemDto> itemDtos = request.items() != null
                ? request.items().stream()
                .map(item -> new CreateOrderCommand.OrderItemDto(
                        item.productId(),
                        item.quantity(),
                        item.price(),
                        item.subTotal()
                ))
                .toList()
                : new ArrayList<>();

        return new CreateOrderCommand(
                request.customerId(),
                request.businessId(),
                request.price(),
                addressDto,
                itemDtos
        );
    }

    public CreateOrderWebResponse toWebResponse(CreateOrderResponse response) {
        return new CreateOrderWebResponse(
                response.orderTrackingId(),
                response.orderStatus(),
                response.message()
        );
    }
}