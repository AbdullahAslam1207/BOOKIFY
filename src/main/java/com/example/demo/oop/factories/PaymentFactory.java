package com.example.demo.oop.factories;

import com.example.demo.database.queries.PaymentQueries;
import com.example.demo.database.queries.DiscountCodeQueries;
import com.example.demo.oop.models.DiscountCode;
import com.example.demo.oop.models.Membership;
import com.example.demo.oop.models.Payment;

import java.util.List;

public class PaymentFactory {
    private final PaymentQueries paymentQueries;

    public PaymentFactory() {
        this.paymentQueries = new PaymentQueries();
    }

    public boolean createPayment(int bookingId, int userId, double amount, String paymentMethod, String status) {
        Membership membership = new MembershipFactory().getActiveMembership(userId);
        if (membership != null) {
            double discount = (amount * membership.getDiscountPercentage()) / 100;
            amount -= discount; // Apply discount
        }
        return paymentQueries.insertPayment(bookingId, userId, amount, paymentMethod, status);
    }

    public boolean creatememPayment(int memId, int userId, double amount, String paymentMethod, String status) {

        return paymentQueries.addPaymentForMembership(memId, userId, amount, paymentMethod, status);
    }
    // Method to apply a discount code during payment
    public boolean applyDiscountCode(int bookingId, int userId, String discountCode) {
        // Step 1: Validate the discount code for the user
        DiscountCodeQueries discountCodeQueries = new DiscountCodeQueries();

        // Use the instance to call the method
        DiscountCode discountDetails = discountCodeQueries.getDiscountCodeByUserAndCode(userId, discountCode);
        //System.out.println("disocunt isused"+discountDetails.isUsed());
        if (discountDetails == null || discountDetails.isUsed()) {
            return false; // Invalid or expired discount code
        }

        // Step 2: Fetch the current payment amount

        Payment payment = paymentQueries.getPaymentById(bookingId);
        if (payment == null) {
            return false;
        }

        // Step 3: Calculate the discounted amount
        double discountedAmount = payment.getAmount() - (payment.getAmount() * discountDetails.getDiscountPercentage() / 100);

        // Step 4: Update the payment amount
        return paymentQueries.updatePaymentWithDiscount(bookingId, discountedAmount);
    }



    public List<Payment> getPendingPayments(int userId) {
        return paymentQueries.getPendingPayments(userId);
    }

    public boolean confirmPayment(int bookingid) {
        return paymentQueries.updatePaymentStatus(bookingid, "success");
    }

    public boolean confirmMembershipPayment(int memid) {
        return paymentQueries.updatePaymentmemStatus(memid, "success");
    }
}
