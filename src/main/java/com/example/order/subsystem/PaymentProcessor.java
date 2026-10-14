package main.java.com.example.order.subsystem;

import java.util.UUID;

public class PaymentProcessor {

    public boolean validatePaymentToken(String paymentToken) {
        return paymentToken != null
                && !paymentToken.isBlank()
                && !paymentToken.startsWith("DECLINED");
    }

    public String charge(String paymentToken, double amount) {
        System.out.printf("[PaymentProcessor] Charging $%.2f to token '%s'...%n", amount, paymentToken);

        if (!validatePaymentToken(paymentToken) || amount <= 0) {
            System.out.println("[PaymentProcessor] ERROR: Payment declined by issuing bank.");
            return null;
        }

        String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        System.out.println("[PaymentProcessor] Payment approved. Transaction ID: " + transactionId);
        return transactionId;
    }

    public boolean refund(String transactionId, double amount) {
        if (transactionId == null || transactionId.isBlank()) {
            return false;
        }
        System.out.printf("[PaymentProcessor] ROLLBACK/REFUND: Refunded $%.2f for Transaction ID: %s%n",
                amount, transactionId);
        return true;
    }
}