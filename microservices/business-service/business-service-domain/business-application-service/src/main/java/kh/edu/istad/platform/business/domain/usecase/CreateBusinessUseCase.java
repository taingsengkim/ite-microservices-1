package kh.edu.istad.platform.business.domain.usecase;

import kh.edu.istad.platform.business.domain.dto.CreateBusinessCommand;
import kh.edu.istad.platform.business.domain.dto.CreateBusinessResponse;
import reactor.core.publisher.Mono;

public interface CreateBusinessUseCase {
    Mono<CreateBusinessResponse> execute(CreateBusinessCommand command);
}