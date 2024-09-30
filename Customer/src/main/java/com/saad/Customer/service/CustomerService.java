package com.saad.Customer.service;

import com.saad.Customer.customer.CustomerModel;

import com.saad.Customer.customer.CustomerRegistrationRequest;
import com.saad.Customer.repository.CustomerRepository;
import com.saad.Fraud.model.FraudCheckResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;

    public void registerCustomer(CustomerRegistrationRequest request) {
        CustomerModel customer = CustomerModel.builder()
                .firstname(request.firstName())
                .lastname(request.lastName())
                .email(request.email())
                .build();
        // todo: check if email valid
        // todo: check if email not taken
        customerRepository.saveAndFlush(customer);

        // Updated to handle a simple boolean response
        Boolean isFraudster = restTemplate.getForObject(
                "http://localhost:200/api/v1/fraud-check/{customerId}",
                Boolean.class,
                customer.getId()
        );

        if (isFraudster) {
            throw new IllegalStateException("fraudster");
        }
    }
}