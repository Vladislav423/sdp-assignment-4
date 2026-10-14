package main.java.com.example.order.subsystem;

import main.java.com.example.order.model.CartItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryService {
    private static final Map<String, Integer> WAREHOUSE_STOCK = new HashMap<>();

    static {
        WAREHOUSE_STOCK.put("LAPTOP-PRO", 5);
        WAREHOUSE_STOCK.put("MOUSE-WL", 20);
        WAREHOUSE_STOCK.put("KEYBOARD-MECH", 8);
        WAREHOUSE_STOCK.put("MONITOR-4K", 3);
    }

    public boolean hasSufficientStock(String productId, int requestedQty) {
        return WAREHOUSE_STOCK.getOrDefault(productId, 0) >= requestedQty;
    }

    public int getAvailableQuantity(String productId) {
        return WAREHOUSE_STOCK.getOrDefault(productId, 0);
    }

    public boolean verifyAndReserveStock(List<CartItem> items) {
        for (CartItem item : items) {
            if (!hasSufficientStock(item.productId(), item.quantity())) {
                System.out.println("[InventoryService] Insufficient stock for " + item.productName()
                        + " (requested: " + item.quantity()
                        + ", available: " + getAvailableQuantity(item.productId()) + ")");
                return false;
            }
        }

        for (CartItem item : items) {
            int updatedStock = WAREHOUSE_STOCK.get(item.productId()) - item.quantity();
            WAREHOUSE_STOCK.put(item.productId(), updatedStock);
            System.out.println("[InventoryService] Reserved " + item.quantity() + "x '"
                    + item.productName() + "' (remaining stock: " + updatedStock + ")");
        }
        return true;
    }

    public void releaseReservedStock(List<CartItem> items) {
        for (CartItem item : items) {
            int restoredStock = WAREHOUSE_STOCK.getOrDefault(item.productId(), 0) + item.quantity();
            WAREHOUSE_STOCK.put(item.productId(), restoredStock);
            System.out.println("[InventoryService] ROLLBACK: Released " + item.quantity() + "x '"
                    + item.productName() + "' back to warehouse (restored stock: " + restoredStock + ")");
        }
    }
}