package kh.edu.istad.platform.business.restapi.exception;

import kh.edu.istad.platform.business.domain.exception.BusinessDomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.net.URI;

@RestControllerAdvice
public class BusinessGlobalExceptionHandler {

    @ExceptionHandler(BusinessDomainException.class)
    public ProblemDetail handleBusinessDomainException(BusinessDomainException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Business Domain Rule Violation");
        problem.setType(URI.create("https://istad.edu.kh/errors/business-domain-violation"));
        return problem;
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ProblemDetail handleValidationException(WebExchangeBindException ex) {
        String detail = ex.getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation failed");

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        problem.setTitle("Invalid Request Payload");
        return problem;
    }
}