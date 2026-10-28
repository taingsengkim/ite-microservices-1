package kh.edu.istad.platform.order.client.adapter;

import kh.edu.istad.platform.order.domain.dto.CustomerResponse;
import kh.edu.istad.platform.order.domain.dto.GivenName;
import kh.edu.istad.platform.order.domain.port.out.client.CustomerServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class CustomerServiceClientAdapter implements CustomerServiceClient {


    private final WebClient.Builder webClientBuilder;

    @Override
    public Flux<String> getCustomerNames() {
        WebClient webClient = webClientBuilder
                .baseUrl("http://customer-service")
                .build();
        var response = webClient.get()
                .uri("/api/v1/customers")
                .retrieve()
                .bodyToFlux(GivenName.class);

        return response.map(GivenName::givenName);
    }

    @Override
    public Flux<CustomerResponse> getCustomers() {
        WebClient webClient = webClientBuilder
                .baseUrl("http://customer-service")
                .build();

        return webClient.get()
                .uri("/api/v1/customers")
                .retrieve()
                .bodyToFlux(CustomerResponse.class);
    }
}
