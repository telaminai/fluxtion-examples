# JUnit Testing Demo

This example demonstrates how to test Fluxtion DataFlow applications using JUnit 5.

## Overview

This project showcases best practices for testing DataFlow-based applications. It includes:

- A simple order processing application with business logic
- Comprehensive JUnit 5 tests demonstrating various testing patterns
- Examples of service mocking using Mockito
- Event feed and sink substitution for testing

## The class under test

```java

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
```

## Key Concepts

### DataFlow is Single-Threaded and Easy to Test

A DataFlow processes events synchronously in a single thread. This makes testing straightforward:

```java
orderProcessor =new

OrderProcessor(paymentService, inventoryService);

// Create and initialize DataFlow
DataFlow dataFlow = DataFlowBuilder.subscribeToNode(orderProcessor)
        .console("Received order: {}")
        .map(o -> orderCounter.incrementAndGet())
        .build();

dataFlow.

init();

// Process an event
dataFlow.

onEvent(order);

// Immediately verify results - no async complexity!
assertEquals(1,orderProcessor.getTotalOrdersProcessed());
```

### Business Logic Without Infrastructure

DataFlow separates business logic from infrastructure code. The `OrderProcessor` class contains pure business logic:

- No database connections
- No message queue clients
- No file I/O
- Just business rules and processing logic

This separation makes testing fast, reliable, and focused on business behavior.

### Service Registration and Mocking

DataFlow uses `registerService()` to inject dependencies. In tests, you can easily substitute mock implementations:

```java

@Mock
private PaymentService paymentService;

@Mock
private InventoryService inventoryService;

@BeforeEach
void setUp() {
    // ... create DataFlow ...

    // Register mock services
    dataFlow.registerService(paymentService);
    dataFlow.registerService(inventoryService);
}

@Test
void testWithMocks() {
    // Setup mock behavior
    when(inventoryService.checkStock(anyString(), anyInt())).thenReturn(true);
    when(paymentService.processPayment(anyString(), anyDouble())).thenReturn(true);

    // Test business logic with mocked dependencies
    dataFlow.onEvent(order);

    // Verify interactions
    verify(paymentService).processPayment("CUST-123", 100.0);
}
```

### Testing with DataFlow.addEventFeed

In production, your application might use `DataConnector` to consume events from Kafka, files, or other sources. In
tests, you can substitute these with `DataFlow.addEventFeed`:

```java

@Test
void testWithEventFeed() {
    // Create test data
    List<Order> orders = List.of(
            new Order("ORD-001", "CUST-123", "PROD-456", 2, 50.0),
            new Order("ORD-002", "CUST-124", "PROD-789", 1, 100.0)
    );

    when(inventoryService.checkStock(anyString(), anyInt())).thenReturn(true);
    when(paymentService.processPayment(anyString(), anyDouble())).thenReturn(true);

    // Create a simple test event feed
    EventFeed testFeed = new EventFeed() {
        @Override
        public void registerSubscriber(DataFlow dataFlow) {
            orders.forEach(dataFlow::onEvent);
        }

        @Override
        public void subscribe(DataFlow dataFlow, Object o) {

        }

        @Override
        public void unSubscribe(DataFlow dataFlow, Object o) {

        }


        @Override
        public void removeAllSubscriptions(DataFlow dataFlow) {

        }
    };

    // Add the feed and subscribe
    dataFlow.addEventFeed(testFeed);

    // Verify results
    assertEquals(2, orderProcessor.getTotalOrdersProcessed());
}
```

This approach lets you test event processing logic without requiring actual Kafka brokers, file systems, or other
infrastructure.

### Testing with DataFlow.addIntSink and addSink

In production, your application might publish results to databases, message queues, or files. In tests, you can capture
outputs using sinks:

```java
private final List<OrderSummary> summaries = new ArrayList<>();

@BeforeEach
void setUp() {
    // ... create DataFlow ...

    // add sink
    dataFlow.registerService(summary -> summaries.add((OrderSummary) summary), MessageSink.class, "summaries");
}

@Test
void testWithObjectSink() {
    // Arrange - setup mock behavior
    when(inventoryService.checkStock(anyString(), anyInt())).thenReturn(true);
    when(paymentService.processPayment(anyString(), anyDouble())).thenReturn(true);

    Order order = new Order("ORD-001", "CUST-123", "PROD-456", 2, 50.0);

    // Process and publish
    dataFlow.onEvent(order);
    dataFlow.publishObjectSignal(orderProcessor.getSummary());

    // Verify captured objects
    assertEquals(1, summaries.size());
    assertEquals(100.0, summaries.get(0).getTotalRevenue());
}
```

This allows you to verify outputs without needing actual sinks like databases or message queues.

## Project Structure

```
junit-testing-demo/
├── src/
│   ├── main/java/
│   │   └── com/telamin/fluxtion/example/sampleapps/testing/
│   │       ├── OrderProcessor.java          # Business logic
│   │       ├── Order.java                   # Event class
│   │       ├── OrderSummary.java            # Data class
│   │       ├── PaymentService.java          # Service interface
│   │       └── InventoryService.java        # Service interface
│   └── test/java/
│       └── com/telamin/fluxtion/example/sampleapps/testing/
│           └── OrderProcessorTest.java      # Comprehensive tests
└── pom.xml
```

## Running the Tests

### Using Maven

```bash
# Run all tests
mvn test

# Run a specific test
mvn test -Dtest=OrderProcessorTest

# Run a specific test method
mvn test -Dtest=OrderProcessorTest#testSuccessfulOrderProcessing
```

### Using IDE

Open the project in your IDE and run the tests using the built-in test runner. The tests use JUnit 5 which is supported
by all modern Java IDEs.

## Test Coverage

The example includes 6 comprehensive tests:

1. **testSuccessfulOrderProcessing** - Basic happy path testing
2. **testOrderFailsDueToInsufficientInventory** - Failure scenario testing
3. **testOrderFailsDueToPaymentDecline** - Another failure scenario
4. **testMultipleOrders** - Batch event processing
5. **testWithDataFlowEvents** - Event processing through DataFlow
6. **testServiceStateChanges** - Stateful service behavior

## Key Testing Patterns

### 1. Arrange-Act-Assert

All tests follow the AAA pattern:

```java

@Test
void test() {
    // Arrange - setup test data and mocks
    when(service.method()).thenReturn(value);

    // Act - execute the code under test
    dataFlow.onEvent(event);

    // Assert - verify the results
    assertEquals(expected, actual);
    verify(service).method();
}
```

### 2. Mock Setup in @BeforeEach

Common setup is done once per test:

```java

@BeforeEach
void setUp() {
    // Create DataFlow
    // Register services
}
```

### 3. Focused Tests

Each test verifies one specific behavior or scenario. Tests are small, focused, and easy to understand.

### 4. Verification of Side Effects

Tests verify both direct results and service interactions:

```java
// Verify state
assertEquals(1,orderProcessor.getTotalOrdersProcessed());

// Verify service calls
verify(paymentService).

processPayment("CUST-123",100.0);

// Verify service was NOT called
verify(inventoryService, never()).

reduceStock(anyString(),anyInt());
```

## Dependencies

The project uses:

- **JUnit 5** (Jupiter) - Testing framework
- **Mockito** - Mocking framework
- **Fluxtion Runtime** - DataFlow runtime
- **Fluxtion Builder** - DataFlow builder API
- **Lombok** - Reduces boilerplate code

## Benefits of This Approach

1. **Fast Tests** - No infrastructure dependencies means tests run in milliseconds
2. **Reliable Tests** - No flaky tests from network, file system, or timing issues
3. **Focused Tests** - Test business logic, not infrastructure
4. **Easy Debugging** - Single-threaded execution makes debugging straightforward
5. **Clear Intent** - Tests clearly show how the system should behave
6. **Refactoring Safety** - Comprehensive tests enable confident refactoring

## Learn More

For a complete guide on unit testing DataFlow applications, see
the [Unit Testing DataFlow](../../fluxtion/docs/how-to/unit-testing-dataflow.md) how-to guide.

## License

This example is part of the Fluxtion Examples project.
