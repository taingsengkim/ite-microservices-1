package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.InitiatedCustomerResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@Slf4j
public class InitiateCustomerUseCase {
    public InitiatedCustomerResult execute(InitiateCustomerCommand command){
        System.out.println(command);
        log.info("Initiate customer usecase : {}" , command);
        return new InitiatedCustomerResult(UUID.randomUUID());
    }
}
