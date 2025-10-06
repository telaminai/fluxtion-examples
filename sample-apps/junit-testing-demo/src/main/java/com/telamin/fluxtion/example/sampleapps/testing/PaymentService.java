package com.telamin.fluxtion.example.sampleapps.testing;

/**
 * Service interface for processing payments.
 * In production, this would connect to a payment gateway.
 * In tests, this can be easily mocked.
 */
public interface PaymentService {
    
    /**
     * Process a payment for a customer.
     * 
     * @param customerId the customer ID
     * @param amount the payment amount
     * @return true if payment successful, false otherwise
     */
    boolean processPayment(String customerId, double amount);
}
