package kh.edu.istad.platform.business.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.business.domain.usecase.CreateBusinessUseCase;
import kh.edu.istad.platform.business.restapi.dto.CreateBusinessRequest;
import kh.edu.istad.platform.business.restapi.dto.CreateBusinessWebResponse;
import kh.edu.istad.platform.business.restapi.mapper.BusinessRestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
public class BusinessController {

    private final CreateBusinessUseCase createBusinessUseCase;
    private final BusinessRestMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CreateBusinessWebResponse> createBusiness(@Valid @RequestBody CreateBusinessRequest request) {
        log.info("Received reactive request to create business: {}", request.legalName());

        return Mono.just(request)
                .map(mapper::toCommand)
                .flatMap(createBusinessUseCase::execute)
                .map(mapper::toWebResponse);
    }
}