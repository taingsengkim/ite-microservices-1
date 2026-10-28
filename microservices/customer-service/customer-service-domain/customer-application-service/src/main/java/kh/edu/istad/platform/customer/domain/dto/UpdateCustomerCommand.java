package kh.edu.istad.platform.customer.client.domain.dto;

public record UpdateCustomerCommand (
        String familyName,
        String givenName
){

}
