package main.java.com.example.order.model;

public record Customer(
        String customerId,
        String fullName,
        String email,
        String shippingAddress,
        String paymentToken
) {}