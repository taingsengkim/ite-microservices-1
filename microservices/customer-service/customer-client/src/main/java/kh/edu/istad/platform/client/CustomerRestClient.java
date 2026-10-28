package kh.edu.istad.platform.customer;

import kh.edu.istad.platform.customer.dto.CustomerResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class CustomerRestClient {
    private final WebClient webClient;

    public CustomerRestClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:9091").build();
    }

    public Mono<CustomerResponse> getCustomer(UUID id) {
        return webClient.get()
                .uri("/api/v1/customers/{id}", id)
                .retrieve()
                .bodyToMono(CustomerResponse.class);
    }
}


