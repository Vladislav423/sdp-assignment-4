package main.java.com.example.order.model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private final List<CartItem> items = new ArrayList<>();

    public void addItem(String productId, String productName, int quantity, double unitPrice) {
        items.add(new CartItem(productId, productName, quantity, unitPrice));
    }

    public List<CartItem> getItems() {
        return List.copyOf(items);
    }

    public double calculateItemsTotal() {
        return items.stream()
                .mapToDouble(CartItem::subtotal)
                .sum();
    }

    public int getTotalUnitCount() {
        return items.stream()
                .mapToInt(CartItem::quantity)
                .sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}