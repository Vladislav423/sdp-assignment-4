package main.java.com.example.order;

import main.java.com.example.order.facade.OrderFacade;
import main.java.com.example.order.facade.StorefrontSupportFacade;
import main.java.com.example.order.model.Customer;
import main.java.com.example.order.model.OrderReceipt;
import main.java.com.example.order.model.ShoppingCart;

public class Main {
    public static void main(String[] args) {
        OrderFacade orderFacade = new OrderFacade();
        StorefrontSupportFacade supportFacade = new StorefrontSupportFacade();

        ShoppingCart cart1 = new ShoppingCart();
        cart1.addItem("LAPTOP-PRO", "16-inch Developer Laptop", 1, 1400.00);
        cart1.addItem("MOUSE-WL", "Ergonomic Wireless Mouse", 2, 35.00);

        System.out.println("=== Pre-Checkout Inquiry via StorefrontSupportFacade ===");
        System.out.println("Available LAPTOP-PRO stock: " + supportFacade.checkAvailableStock("LAPTOP-PRO"));
        double estimatedTotal = supportFacade.estimateTotalWithShipping(cart1, "Mangilik El Ave 55/11, Astana");
        System.out.printf("Estimated Order Total (with shipping): $%.2f%n", estimatedTotal);

        Customer validCustomer = new Customer(
                "CUST-01",
                "Alikhan Bokeikhanov",
                "alikhan@astanait.edu.kz",
                "Mangilik El Ave 55/11, Astana",
                "VALID-VISA-4242"
        );
        OrderReceipt receipt1 = orderFacade.placeOrder(cart1, validCustomer);
        System.out.println("Result 1 -> " + receipt1);

        ShoppingCart cart2 = new ShoppingCart();
        cart2.addItem("KEYBOARD-MECH", "Mechanical Keyboard RGB", 2, 110.00);

        Customer declinedCardCustomer = new Customer(
                "CUST-02",
                "Dana Nurpeisova",
                "dana@astanait.edu.kz",
                "Turan Ave 37, Astana",
                "DECLINED-CARD-0000"
        );
        OrderReceipt receipt2 = orderFacade.placeOrder(cart2, declinedCardCustomer);
        System.out.println("Result 2 -> " + receipt2);

        ShoppingCart cart3 = new ShoppingCart();
        cart3.addItem("MONITOR-4K", "27-inch 4K UHD Monitor", 1, 450.00);

        Customer restrictedAddressCustomer = new Customer(
                "CUST-03",
                "Timur Suleimenov",
                "timur@astanait.edu.kz",
                "RESTRICTED-ZONE-ANTARCTICA",
                "VALID-MASTERCARD-8888"
        );
        OrderReceipt receipt3 = orderFacade.placeOrder(cart3, restrictedAddressCustomer);
        System.out.println("Result 3 -> " + receipt3);

        boolean cancelled = orderFacade.cancelOrder(receipt1.orderId());
        System.out.println("Cancellation status for " + receipt1.orderId() + ": " + cancelled);
    }
}