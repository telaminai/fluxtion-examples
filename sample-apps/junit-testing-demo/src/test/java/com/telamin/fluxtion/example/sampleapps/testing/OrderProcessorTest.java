package com.telamin.fluxtion.example.sampleapps.testing;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;
import com.telamin.fluxtion.runtime.service.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Demonstrates how to test a DataFlow with JUnit 5.
 * 
 * Key points covered:
 * 1. DataFlow is single-threaded and easy to test
 * 2. Business logic testing without infrastructure code  
 * 3. Service registration and mocking with DataFlow.registerService
 * 4. Using sinks to capture outputs with DataFlow.addIntSink
 */
@ExtendWith(MockitoExtension.class)
class OrderProcessorTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private InventoryService inventoryService;

    private DataFlow dataFlow;
    private OrderProcessor orderProcessor;
    private AtomicInteger orderCounter = new AtomicInteger(0);

    @BeforeEach
    void setUp() {
        // Create OrderProcessor instance with mock services injected
        // This demonstrates how easy it is to test business logic with mocked dependencies
        orderProcessor = new OrderProcessor(paymentService, inventoryService);

        orderCounter.set(0);
        
        // Create DataFlow subscribing to Order events
        // The orderProcessor::processOrder method will be called for each Order event
        dataFlow = DataFlowBuilder.subscribeToNode(orderProcessor)
                .console("Received order: {}")
                .map(o -> orderCounter.incrementAndGet())
                .build();
        
        dataFlow.init();
        
        // Register mock services with DataFlow - this is a key testing pattern
        // Services can be easily mocked and injected for testing
        dataFlow.registerService(new Service<>(paymentService, PaymentService.class));
        dataFlow.registerService(new Service<>(inventoryService, InventoryService.class));
    }

    /**
     * Test 1: Basic DataFlow testing with mocked services.
     * 
     * DataFlow is single-threaded, making it easy to test.
     * Events are processed synchronously, so assertions can be made immediately.
     */
    @Test
    void testSuccessfulOrderProcessing() {
        // Arrange - setup mock behavior
        when(inventoryService.checkStock(anyString(), anyInt())).thenReturn(true);
        when(paymentService.processPayment(anyString(), anyDouble())).thenReturn(true);
        
        Order order = new Order("ORD-001", "CUST-123", "PROD-456", 2, 50.0);
        
        // Act - process order through OrderProcessor directly
        dataFlow.onEvent(order);

        // Assert - verify business logic executed correctly
        assertEquals(1, orderCounter.get());
        assertEquals(1, orderProcessor.getTotalOrdersProcessed());
        assertEquals(100.0, orderProcessor.getTotalRevenue(), 0.01);
        assertEquals(0, orderProcessor.getFailedOrders());
        
        // Verify service interactions
        verify(inventoryService).checkStock("PROD-456", 2);
        verify(paymentService).processPayment("CUST-123", 100.0);
        verify(inventoryService).reduceStock("PROD-456", 2);
    }

    /**
     * Test 2: Testing failure scenarios - insufficient inventory.
     * 
     * Business logic can be tested independently of infrastructure.
     */
    @Test
    void testOrderFailsDueToInsufficientInventory() {
        // Arrange - simulate out of stock
        when(inventoryService.checkStock(anyString(), anyInt())).thenReturn(false);
        
        Order order = new Order("ORD-002", "CUST-123", "PROD-456", 10, 50.0);
        
        // Act
        dataFlow.onEvent(order);
        
        // Assert
        assertEquals(0, orderCounter.get());
        assertEquals(0, orderProcessor.getTotalOrdersProcessed());
        assertEquals(0.0, orderProcessor.getTotalRevenue());
        assertEquals(1, orderProcessor.getFailedOrders());
        
        // Verify payment was never attempted
        verify(inventoryService).checkStock("PROD-456", 10);
        verify(paymentService, never()).processPayment(anyString(), anyDouble());
    }

    /**
     * Test 3: Testing failure scenarios - payment declined.
     */
    @Test
    void testOrderFailsDueToPaymentDecline() {
        // Arrange
        when(inventoryService.checkStock(anyString(), anyInt())).thenReturn(true);
        when(paymentService.processPayment(anyString(), anyDouble())).thenReturn(false);
        
        Order order = new Order("ORD-003", "CUST-123", "PROD-456", 2, 50.0);
        
        // Act
        dataFlow.onEvent(order);
        
        // Assert
        assertEquals(0, orderCounter.get());
        assertEquals(0, orderProcessor.getTotalOrdersProcessed());
        assertEquals(1, orderProcessor.getFailedOrders());
        
        // Verify inventory was NOT reduced since payment failed
        verify(inventoryService).checkStock("PROD-456", 2);
        verify(paymentService).processPayment("CUST-123", 100.0);
        verify(inventoryService, never()).reduceStock(anyString(), anyInt());
    }

    /**
     * Test 4: Testing with multiple orders.
     * 
     * DataFlow processes events synchronously, making batch testing straightforward.
     */
    @Test
    void testMultipleOrders() {
        // Arrange
        when(inventoryService.checkStock(anyString(), anyInt())).thenReturn(true);
        when(paymentService.processPayment(anyString(), anyDouble())).thenReturn(true);
        
        // Act - process multiple orders
        dataFlow.onEvent(new Order("ORD-001", "CUST-123", "PROD-456", 2, 50.0));
        dataFlow.onEvent(new Order("ORD-002", "CUST-124", "PROD-789", 1, 100.0));
        dataFlow.onEvent(new Order("ORD-003", "CUST-125", "PROD-456", 3, 50.0));
        
        // Assert
        assertEquals(3, orderProcessor.getTotalOrdersProcessed());
        assertEquals(350.0, orderProcessor.getTotalRevenue(), 0.01);
        assertEquals(0, orderProcessor.getFailedOrders());
    }

    /**
     * Test 5: Testing with event processing through DataFlow.
     * 
     * In production, events might come from Kafka, files, etc.
     * In tests, we can send events directly to DataFlow.
     */
    @Test
    void testWithDataFlowEvents() {
        // Arrange
        when(inventoryService.checkStock(anyString(), anyInt())).thenReturn(true);
        when(paymentService.processPayment(anyString(), anyDouble())).thenReturn(true);
        
        // Create test data
        List<Order> orders = List.of(
            new Order("ORD-001", "CUST-123", "PROD-456", 2, 50.0),
            new Order("ORD-002", "CUST-124", "PROD-789", 1, 100.0)
        );
        
        // Act - send events through DataFlow
        // In tests, we manually send events. In production, these come from DataConnector
        orders.forEach(order -> dataFlow.onEvent(order));
        
        // Assert
        assertEquals(2, orderProcessor.getTotalOrdersProcessed());
        assertEquals(200.0, orderProcessor.getTotalRevenue(), 0.01);
    }

    /**
     * Test 6: Testing with service state changes.
     * 
     * Mocks can simulate state changes between events.
     */
    @Test
    void testServiceStateChanges() {
        // Arrange - first order succeeds, second fails due to inventory depletion
        when(inventoryService.checkStock("PROD-456", 2))
            .thenReturn(true)   // First call succeeds
            .thenReturn(false); // Second call fails
        when(paymentService.processPayment(anyString(), anyDouble())).thenReturn(true);
        
        // Act
        dataFlow.onEvent(new Order("ORD-001", "CUST-123", "PROD-456", 2, 50.0));
        dataFlow.onEvent(new Order("ORD-002", "CUST-124", "PROD-456", 2, 50.0));
        
        // Assert
        assertEquals(1, orderProcessor.getTotalOrdersProcessed());
        assertEquals(1, orderProcessor.getFailedOrders());
        assertEquals(100.0, orderProcessor.getTotalRevenue(), 0.01);
    }
}
