package main.java.com.example.order.facade;

import main.java.com.example.order.model.CartItem;
import main.java.com.example.order.model.Customer;
import main.java.com.example.order.model.OrderReceipt;
import main.java.com.example.order.model.ShoppingCart;
import main.java.com.example.order.subsystem.InventoryService;
import main.java.com.example.order.subsystem.NotificationService;
import main.java.com.example.order.subsystem.PaymentProcessor;
import main.java.com.example.order.subsystem.ShippingService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderFacade {
    private static final AtomicInteger ORDER_SEQUENCE = new AtomicInteger(1000);

    private final InventoryService inventoryService;
    private final PaymentProcessor paymentProcessor;
    private final ShippingService shippingService;
    private final NotificationService notificationService;
    private final Map<String, ActiveOrderRecord> activeOrders = new HashMap<>();

    public OrderFacade() {
        this.inventoryService = new InventoryService();
        this.paymentProcessor = new PaymentProcessor();
        this.shippingService = new ShippingService();
        this.notificationService = new NotificationService();
    }

    public OrderReceipt placeOrder(ShoppingCart cart, Customer customer) {
        String orderId = "ORD-" + ORDER_SEQUENCE.incrementAndGet();
        System.out.println("\n==================================================");
        System.out.println("[OrderFacade] Starting order placement: " + orderId
                + " for customer " + customer.fullName());

        if (cart == null || cart.isEmpty()) {
            return new OrderReceipt(false, orderId, null, null, 0.0, "Shopping cart cannot be empty.");
        }

        boolean stockReserved = inventoryService.verifyAndReserveStock(cart.getItems());
        if (!stockReserved) {
            notificationService.sendFailureNotification(
                    customer.email(), orderId, "Requested items are out of stock.");
            return new OrderReceipt(false, orderId, null, null, 0.0, "Order failed: Insufficient stock.");
        }

        double shippingFee = Math.max(0.0,
                shippingService.calculateShippingCost(customer.shippingAddress(), cart.getTotalUnitCount()));
        double totalAmount = cart.calculateItemsTotal() + shippingFee;

        String transactionId = paymentProcessor.charge(customer.paymentToken(), totalAmount);
        if (transactionId == null) {
            System.out.println("[OrderFacade] Payment failed! Rolling back reserved inventory...");
            inventoryService.releaseReservedStock(cart.getItems());
            notificationService.sendFailureNotification(
                    customer.email(), orderId, "Payment declined. Reserved stock has been released.");
            return new OrderReceipt(false, orderId, null, null, 0.0,
                    "Order failed: Payment declined (inventory rolled back).");
        }

        String trackingNumber = shippingService.createShipment(
                orderId, customer.shippingAddress(), cart.getTotalUnitCount());
        if (trackingNumber == null) {
            System.out.println("[OrderFacade] Shipping creation failed! Rolling back payment and inventory...");
            paymentProcessor.refund(transactionId, totalAmount);
            inventoryService.releaseReservedStock(cart.getItems());
            notificationService.sendFailureNotification(
                    customer.email(), orderId, "Unserviceable shipping address. Payment refunded.");
            return new OrderReceipt(false, orderId, transactionId, null, 0.0,
                    "Order failed: Shipping unavailable (payment and inventory rolled back).");
        }

        activeOrders.put(orderId, new ActiveOrderRecord(
                orderId, customer, cart.getItems(), transactionId, trackingNumber, totalAmount));
        notificationService.sendOrderConfirmation(customer.email(), orderId, trackingNumber, totalAmount);

        return new OrderReceipt(true, orderId, transactionId, trackingNumber, totalAmount,
                "Order placed successfully.");
    }

    public boolean cancelOrder(String orderId) {
        System.out.println("\n==================================================");
        System.out.println("[OrderFacade] Requesting cancellation for Order: " + orderId);

        ActiveOrderRecord record = activeOrders.remove(orderId);
        if (record == null) {
            System.out.println("[OrderFacade] Order " + orderId + " not found or already cancelled.");
            return false;
        }

        shippingService.cancelShipment(record.trackingNumber());
        paymentProcessor.refund(record.transactionId(), record.totalCharged());
        inventoryService.releaseReservedStock(record.items());
        notificationService.sendCancellationConfirmation(
                record.customer().email(), record.orderId(), record.totalCharged());
        return true;
    }

    private record ActiveOrderRecord(
            String orderId,
            Customer customer,
            List<CartItem> items,
            String transactionId,
            String trackingNumber,
            double totalCharged
    ) {}
}