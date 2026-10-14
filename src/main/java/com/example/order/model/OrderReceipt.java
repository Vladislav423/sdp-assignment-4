package main.java.com.example.order.model;

public record OrderReceipt(
        boolean success,
        String orderId,
        String transactionId,
        String trackingNumber,
        double totalCharged,
        String message
) {}