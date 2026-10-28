package kh.edu.istad.platform.order;

import kh.edu.istad.platform.order.config.ServicePropConfig;
import kh.edu.istad.platform.order.domain.dto.CustomerResponse;
import kh.edu.istad.platform.order.domain.port.out.client.CustomerServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@RefreshScope
@RestController
@RequiredArgsConstructor
public class WebFluxController {


    @GetMapping("/flux")
    public Flux<String> getFluxData(){
        return Flux.just("A","B","C","D","E","F").log().delayElements(Duration.ofSeconds(1));
    }


    private final CustomerServiceClient customerServiceClient;

    @GetMapping("/name")
    public Flux<String> getCustomerNames(){
        return customerServiceClient.getCustomerNames();
    }

    @GetMapping("/customers")
    public Flux<CustomerResponse> getCustomers(){
        return customerServiceClient.getCustomers();
    }

    private final ServicePropConfig servicePropConfig;
//    @Value("${global.version}")
//    private String globalVersion;

    @GetMapping("/consul/config")
    public Mono<Map<String,Object>> getConsulConfig() {
//        return Mono.just(
//                Map.of("message",servicePropConfig.getMessage(),
//                        "version",servicePropConfig.getVersion(),
//                        "globalVersion",globalVersion,
//                        "env",servicePropConfig.getEnv(),
//                        "baseUrl",servicePropConfig.getBaseUrl())
//        );
        return Mono.just(
                Map.of(
//                        "globalVersion",globalVersion,
                        "consulConfig",servicePropConfig)
        );
    }
}


