package main.java.com.example.order.subsystem;

public class NotificationService {

    public void sendOrderConfirmation(String email, String orderId, String trackingNumber, double totalCharged) {
        System.out.printf("[NotificationService] Email -> %s | Order %s confirmed! Charged: $%.2f, Tracking: %s%n",
                email, orderId, totalCharged, trackingNumber);
    }

    public void sendFailureNotification(String email, String orderId, String reason) {
        System.out.println("[NotificationService] Email -> " + email
                + " | Order " + orderId + " failed: " + reason);
    }

    public void sendCancellationConfirmation(String email, String orderId, double refundedAmount) {
        System.out.printf("[NotificationService] Email -> %s | Order %s cancelled. Refund of $%.2f initiated.%n",
                email, orderId, refundedAmount);
    }
}