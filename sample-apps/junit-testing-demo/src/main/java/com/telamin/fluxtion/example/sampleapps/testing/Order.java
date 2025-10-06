package com.telamin.fluxtion.example.sampleapps.testing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Order event representing a customer order.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String orderId;
    private String customerId;
    private String productId;
    private int quantity;
    private double price;
}
