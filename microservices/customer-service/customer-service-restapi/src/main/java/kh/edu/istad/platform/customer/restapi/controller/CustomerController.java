package kh.edu.istad.platform.customer.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.CustomerSegmentId;
import kh.edu.istad.platform.customer.domain.dto.CustomerFilter;
import kh.edu.istad.platform.customer.domain.dto.CustomerSegmentFilter;
import kh.edu.istad.platform.customer.domain.dto.CustomerStatistics;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerSegmentCommand;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.usecase.*;
import kh.edu.istad.platform.customer.restapi.dto.*;
import kh.edu.istad.platform.customer.restapi.mapper.CustomerWebMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final GetCustomerByIdUseCase getCustomerByIdUseCase;
    private final DeleteCustomerUseCase deleteCustomerUseCase;
    private final GetAllCustomerSegmentsUseCase getAllCustomerSegmentsUseCase;
    private final GetCustomerSegmentByIdUseCase getCustomerSegmentByIdUseCase;
    private final UpdateCustomerSegmentUseCase updateCustomerSegmentUseCase;
    private final DeleteCustomerSegmentUseCase deleteCustomerSegmentUseCase;
    private final QueryCustomerSegmentUseCase queryCustomerSegmentUseCase;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerInitiateResponse> initiateCustomer(@Valid @RequestBody CustomerInitiateRequest customerInitiateRequest) {
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

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable("id") UUID id) {
        CustomerId customerId = new CustomerId(id);

        return deleteCustomerUseCase.deleteCustomer(customerId)
                .thenReturn(ResponseEntity.noContent().build());
    }


    @GetMapping("/{id}")
    public Mono<CustomerResponse> getCustomerById(@PathVariable("id") UUID id) {
        CustomerId customerId = new CustomerId(id);
        return getCustomerByIdUseCase.getCustomerById(customerId)
                .map(customerWebMapper::toCustomerResponse);
    }


    @PostMapping("/segment")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerSegmentResponse> initiateCustomerSegment(@Valid @RequestBody  CustomerSegmentRequest customerSegmentRequest) {
        return createCustomerSegmentUseCase.execute(customerWebMapper.toCustomerSegment(customerSegmentRequest)).map(customerWebMapper::toCustomerSegmentResponse);
    }

    @GetMapping("/segment")
    public Flux<CustomerSegmentResponse> getAllCustomerSegment(){
        return getAllCustomerSegmentsUseCase.execute().map(customerWebMapper::toCustomerSegmentResponse);
    }

    @GetMapping("/segment/{id}")
    public Mono<CustomerSegmentResponse> getCustomerSegmentById(@PathVariable("id") UUID id) {
        return getCustomerSegmentByIdUseCase.execute(new CustomerSegmentId(id))
                .map(customerWebMapper::toCustomerSegmentResponse);
    }

    @PatchMapping("/segment/{id}")
    public Mono<CustomerSegmentResponse> updateCustomerSegment(@PathVariable("id") UUID id, @Valid @RequestBody CustomerSegmentUpdateRequest customerSegmentUpdateRequest) {
        return updateCustomerSegmentUseCase.execute(new CustomerSegmentId(id),customerWebMapper.toUpdateCustomerSegmentCommand(customerSegmentUpdateRequest))
                .map(customerWebMapper::toCustomerSegmentResponse);
    }

    @DeleteMapping("/segment/{id}")
    public Mono<Void> hardDeleteCustomerSegment(@PathVariable("id") UUID id){
        return deleteCustomerSegmentUseCase.execute(new CustomerSegmentId(id));
    }
    @GetMapping("/segment/search")
    public Flux<CustomerSegmentResponse> queryCustomerSegments(
            @RequestParam(name = "type", required = false) String type) {

        CustomerSegmentFilter filter = new CustomerSegmentFilter(type);

        return queryCustomerSegmentUseCase.execute(filter)
                .map(customerWebMapper::toCustomerSegmentResponse);
    }

    @GetMapping("/statistics")
    public Mono<CustomerStatistics> getStatistics() {
        return getCustomerStatisticsUseCase.execute();
    }

}