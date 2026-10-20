package kh.edu.istad.platform.customer.restapi.controller;

import kh.edu.istad.platform.customer.domain.dto.CustomerFilter;
import kh.edu.istad.platform.customer.domain.usecase.CreateCustomerSegmentUseCase;
import kh.edu.istad.platform.customer.domain.usecase.CreateCustomerUseCase;
import kh.edu.istad.platform.customer.domain.usecase.QueryCustomerUseCase;
import kh.edu.istad.platform.customer.restapi.dto.*;
import kh.edu.istad.platform.customer.restapi.mapper.CustomerWebMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@Slf4j
public class CustomerController {

    private final CustomerWebMapper customerWebMapper;
    private final CreateCustomerUseCase createCustomerUseCase;
    private final QueryCustomerUseCase queryCustomerUseCase;
    private final CreateCustomerSegmentUseCase createCustomerSegmentUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerInitiateResponse> initiateCustomer(@RequestBody CustomerInitiateRequest customerInitiateRequest) {
        return createCustomerUseCase
                .create(customerWebMapper.toCustomer(customerInitiateRequest))
                .map(customerWebMapper::toInitiatedResponse);
    }

    @GetMapping
    public Flux<CustomerResponse> findAll() {
        return queryCustomerUseCase.findAll().map(customerWebMapper::toCustomerResponse);
    }
    @GetMapping("/search")
    public Flux<CustomerResponse> queryCustomers(
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "familyName", required = false) String familyName,
            @RequestParam(name = "givenName", required = false) String givenName,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(name = "customerStatus", required = false) String customerStatus) {
        CustomerFilter filter = new CustomerFilter(
                username,
                familyName,
                givenName,
                email,
                phoneNumber,
                customerStatus
        );

        return queryCustomerUseCase.query(filter).map(customerWebMapper::toCustomerResponse);
    }

    @PostMapping("/segment")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerSegmentResponse> initiateCustomerSegment(@RequestBody CustomerSegmentRequest customerSegmentRequest) {
        return createCustomerSegmentUseCase.execute(customerWebMapper.toCustomerSegment(customerSegmentRequest)).map(customerWebMapper::toCustomerSegmentResponse);
    }
}