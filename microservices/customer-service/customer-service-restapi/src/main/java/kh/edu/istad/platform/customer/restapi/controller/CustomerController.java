package kh.edu.istad.platform.customer.restapi.controller;

import kh.edu.istad.platform.customer.domain.usecase.CreateCustomerUseCase;
import kh.edu.istad.platform.customer.domain.usecase.QueryCustomerUseCase;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateResponse;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerInitiateResponse> initiateCustomer(@RequestBody CustomerInitiateRequest customerInitiateRequest) {
        return createCustomerUseCase
                .create(customerWebMapper.toCustomer(customerInitiateRequest))
                .map(customerWebMapper::toCustomerInitiateResponse);
    }

    @GetMapping
    public Flux<CustomerInitiateResponse> findAll(){
        return queryCustomerUseCase.findAll().map(d->customerWebMapper.toCustomerInitiateResponse(d));
    }
}