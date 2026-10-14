package main.java.com.example.order.model;

public record CartItem(String productId, String productName, int quantity, double unitPrice) {
    public CartItem {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative.");
        }
    }

    public double subtotal() {
        return quantity * unitPrice;
    }
}