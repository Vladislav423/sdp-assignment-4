package main.java.com.example.order.facade;


import main.java.com.example.order.model.ShoppingCart;
import main.java.com.example.order.subsystem.InventoryService;
import main.java.com.example.order.subsystem.ShippingService;

public class StorefrontSupportFacade {
    private final InventoryService inventoryService;
    private final ShippingService shippingService;

    public StorefrontSupportFacade() {
        this.inventoryService = new InventoryService();
        this.shippingService = new ShippingService();
    }

    public int checkAvailableStock(String productId) {
        return inventoryService.getAvailableQuantity(productId);
    }

    public double estimateTotalWithShipping(ShoppingCart cart, String destinationAddress) {
        double shippingCost = shippingService.calculateShippingCost(
                destinationAddress, cart.getTotalUnitCount());
        if (shippingCost < 0) {
            throw new IllegalArgumentException("Destination address is not serviceable: " + destinationAddress);
        }
        return cart.calculateItemsTotal() + shippingCost;
    }
}