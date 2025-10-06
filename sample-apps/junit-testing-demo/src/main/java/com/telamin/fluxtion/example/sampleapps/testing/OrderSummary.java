package com.telamin.fluxtion.example.sampleapps.testing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Summary of order processing metrics.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSummary {
    private int totalOrdersProcessed;
    private double totalRevenue;
    private int failedOrders;
}
