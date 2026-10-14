package main.java.com.example.order.subsystem;

import java.util.UUID;

public class ShippingService {
    private static final double BASE_FEE = 10.0;
    private static final double FEE_PER_UNIT = 2.5;

    public boolean isAddressServiceable(String shippingAddress) {
        return shippingAddress != null
                && !shippingAddress.isBlank()
                && !shippingAddress.toUpperCase().contains("RESTRICTED");
    }

    public double calculateShippingCost(String shippingAddress, int totalUnits) {
        if (!isAddressServiceable(shippingAddress)) {
            return -1.0;
        }
        return BASE_FEE + (totalUnits * FEE_PER_UNIT);
    }

    public String createShipment(String orderId, String shippingAddress, int totalUnits) {
        if (!isAddressServiceable(shippingAddress)) {
            System.out.println("[ShippingService] ERROR: Address '" + shippingAddress
                    + "' is outside serviceable delivery zones.");
            return null;
        }

        String trackingNumber = "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        System.out.println("[ShippingService] Shipment created for Order " + orderId
                + " (" + totalUnits + " units) to '" + shippingAddress
                + "'. Tracking: " + trackingNumber);
        return trackingNumber;
    }

    public void cancelShipment(String trackingNumber) {
        System.out.println("[ShippingService] Shipment with tracking " + trackingNumber + " has been cancelled.");
    }
}