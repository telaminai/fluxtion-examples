package com.telamin.fluxtion.example.sampleapps.testing;

import com.telamin.fluxtion.runtime.annotations.OnEventHandler;
import com.telamin.fluxtion.runtime.annotations.builder.Inject;
import lombok.Data;

/**
 * Business logic component that processes orders.
 * This demonstrates a typical DataFlow node that can be easily tested.
 */
@Data
public class OrderProcessor {
    
    @Inject
    private PaymentService paymentService;
    
    @Inject
    private InventoryService inventoryService;
    
    private int totalOrdersProcessed = 0;
    private double totalRevenue = 0.0;
    private int failedOrders = 0;
    
    /**
     * Constructor for easy testing - allows manual service injection
     */
    public OrderProcessor(PaymentService paymentService, InventoryService inventoryService) {
        this.paymentService = paymentService;
        this.inventoryService = inventoryService;
    }
    
    /**
     * Default constructor for framework use
     */
    public OrderProcessor() {
    }
    
    @OnEventHandler
    public boolean processOrder(Order order) {
        // Check inventory
        if (!inventoryService.checkStock(order.getProductId(), order.getQuantity())) {
            failedOrders++;
            return false;
        }
        
        // Process payment
        double totalAmount = order.getPrice() * order.getQuantity();
        if (!paymentService.processPayment(order.getCustomerId(), totalAmount)) {
            failedOrders++;
            return false;
        }
        
        // Update inventory
        inventoryService.reduceStock(order.getProductId(), order.getQuantity());
        
        // Update metrics
        totalOrdersProcessed++;
        totalRevenue += totalAmount;
        
        return true;
    }
    
    public OrderSummary getSummary() {
        return new OrderSummary(totalOrdersProcessed, totalRevenue, failedOrders);
    }
}
