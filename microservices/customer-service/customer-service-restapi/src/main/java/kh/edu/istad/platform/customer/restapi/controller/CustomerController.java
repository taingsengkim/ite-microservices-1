package kh.edu.istad.platform.customer.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.dto.CustomerFilter;
import kh.edu.istad.platform.customer.domain.dto.CustomerStatistics;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.usecase.*;
import kh.edu.istad.platform.customer.restapi.dto.*;
import kh.edu.istad.platform.customer.restapi.mapper.CustomerWebMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@Slf4j
public class CustomerController {

    private final CustomerWebMapper customerWebMapper;
    private final CreateCustomerUseCase createCustomerUseCase;
    private final QueryCustomerUseCase queryCustomerUseCase;
    private final CreateCustomerSegmentUseCase createCustomerSegmentUseCase;
    private final GetCustomerStatisticsUseCase getCustomerStatisticsUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;

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

    @PatchMapping("/{id}")
    public Mono<CustomerResponse> updateCustomer(@PathVariable("id") UUID id, @Valid @RequestBody CustomerUpdateRequest customerUpdateRequest){
        CustomerId customerId = new CustomerId(id);
        return updateCustomerUseCase.updateCustomer(customerId,customerWebMapper.toUpdateCustomerCommand(customerUpdateRequest)).map(customerWebMapper::toCustomerResponse);
    }

    @PostMapping("/segment")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerSegmentResponse> initiateCustomerSegment(@RequestBody CustomerSegmentRequest customerSegmentRequest) {
        return createCustomerSegmentUseCase.execute(customerWebMapper.toCustomerSegment(customerSegmentRequest)).map(customerWebMapper::toCustomerSegmentResponse);
    }

    @GetMapping("/statistics")
    public Mono<CustomerStatistics> getStatistics() {
        return getCustomerStatisticsUseCase.execute();
    }

}