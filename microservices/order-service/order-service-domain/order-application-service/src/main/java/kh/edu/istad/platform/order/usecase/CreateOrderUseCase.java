package kh.edu.istad.platform.order.usecase;
import kh.edu.istad.platform.order.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.dto.CreateOrderResponse;

public interface CreateOrderUseCase {
    CreateOrderResponse execute(CreateOrderCommand command);
}