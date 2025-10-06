package com.telamin.fluxtion.example.sampleapps.testing;

import com.telamin.fluxtion.runtime.annotations.OnEventHandler;
import com.telamin.fluxtion.runtime.annotations.runtime.ServiceRegistered;
import com.telamin.fluxtion.runtime.output.MessageSink;
import lombok.Data;

/**
 * Business logic component that processes orders.
 * This demonstrates a typical DataFlow node that can be easily tested.
 */
@Data
public class OrderProcessor {

    private PaymentService paymentService;
    private InventoryService inventoryService;

    private int totalOrdersProcessed = 0;
    private double totalRevenue = 0.0;
    private int failedOrders = 0;
    private MessageSink<OrderSummary> summarySink;

    @ServiceRegistered
    public void registerServices(PaymentService paymentService, String name) {
        this.paymentService = paymentService;
    }

    @ServiceRegistered
    public void registerServices(InventoryService inventoryService, String name) {
        this.inventoryService = inventoryService;
    }

    @ServiceRegistered
    public void registerServices(MessageSink<OrderSummary> summarySink, String name) {
        this.summarySink = summarySink;
    }

    @OnEventHandler
    public boolean processOrder(Order order) {
        // Check inventory
        if (!inventoryService.checkStock(order.getProductId(), order.getQuantity())) {
            failedOrders++;
            publishSummary();
            return false;
        }

        // Process payment
        double totalAmount = order.getPrice() * order.getQuantity();
        if (!paymentService.processPayment(order.getCustomerId(), totalAmount)) {
            failedOrders++;
            publishSummary();
            return false;
        }

        // Update inventory
        inventoryService.reduceStock(order.getProductId(), order.getQuantity());

        // Update metrics
        totalOrdersProcessed++;
        totalRevenue += totalAmount;

        publishSummary();
        return true;
    }

    public OrderSummary getSummary() {
        return new OrderSummary(totalOrdersProcessed, totalRevenue, failedOrders);
    }

    private void publishSummary() {
        if (summarySink != null) {
            summarySink.accept(getSummary());
        }
    }
}
