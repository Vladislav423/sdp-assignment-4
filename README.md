# Assignment #4 — Facade Design Pattern (Java 17)

**Course:** ShP-2216 – Software Design Patterns (OP 6B06102)  
**Topic:** Option B — Online Order Processing Facade (`OrderFacade` & `StorefrontSupportFacade`)  
**JDK Version:** Java 17

## 1. Overview
This project implements the **Facade Structural Design Pattern** for an online order processing system.  
Instead of requiring the client (`Main`) to manually instantiate and coordinate multiple backend services in a strict sequence, the project provides:
1. **`OrderFacade` (Main Facade):** Exposes `placeOrder(ShoppingCart cart, Customer customer)` and `cancelOrder(String orderId)`, coordinating stock reservation, payment charging, shipment scheduling, customer notifications, and multi-step rollbacks on failure.
2. **`StorefrontSupportFacade` (Additional Facade):** Exposes read-only pre-checkout methods (`checkAvailableStock`, `estimateTotalWithShipping`) without polluting `OrderFacade`.

## 2. Project Structure
```text
src/main/java/com/example/order/
├── Main.java                             # Client (uses only Facades and Models)
├── facade/
│   ├── OrderFacade.java                  # Main Facade for order lifecycle
│   └── StorefrontSupportFacade.java      # Additional Facade for pre-checkout queries
├── model/
│   ├── CartItem.java                     # Immutable item record
│   ├── Customer.java                     # Immutable customer record
│   ├── OrderReceipt.java                 # Immutable execution receipt record
│   └── ShoppingCart.java                 # Cart domain entity
└── subsystem/
    ├── InventoryService.java             # Stock verification, reservation, and rollback
    ├── PaymentProcessor.java             # Payment validation, charging, and refunds
    ├── ShippingService.java              # Address check, fee calculation, label creation
    └── NotificationService.java          # Email alerts and confirmations
```

## 3. How to Run
Open the repository in IntelliJ IDEA (ensure JDK 17 is selected in Project Structure -> Project SDK).

Run src.main.java.com.example.order.Main.

Inspect the console output demonstrating:

Pre-checkout stock & shipping estimation via StorefrontSupportFacade.

Scenario 1: Successful order placement across all 4 subsystem services.

Scenario 2: Declined payment triggering automatic inventory reservation rollback.

Scenario 3: Unserviceable shipping zone triggering both payment refund and inventory rollback.

Scenario 4: Order cancellation via cancelOrder(orderId).