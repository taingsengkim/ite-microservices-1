package kh.edu.istad.platform.order.restapi.controller;

import kh.edu.istad.platform.client.CustomerRestClient;
import kh.edu.istad.platform.client.dto.CustomerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final CustomerRestClient customerRestClient;
    // Part 1 Test Endpoint (Hard-coded communication)
    @GetMapping("/test-customer/{id}")
    public Mono<CustomerResponse> testCustomerCall(@PathVariable("id") UUID id) {
        // Calls http://localhost:9091/api/v1/customers/{id} directly
        return customerRestClient.getCustomer(id);
    }
    @GetMapping()
    public void testCustomerCall() {
            log.info("Hello");
    }
}