package com.example.demo.controllers;

import com.example.demo.oop.factories.BookingFactory;
import com.example.demo.oop.factories.MembershipFactory;
import com.example.demo.oop.factories.NotificationFactory;
import com.example.demo.oop.factories.PaymentFactory;
import com.example.demo.oop.models.Payment;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class PaymentController {

    @FXML
    private VBox paymentListContainer;

    @FXML
    private TextField bookingIdField, amountField,membershipIdField;;

    @FXML
    private ComboBox<String> paymentMethodComboBox;

    @FXML
    private RadioButton bookingPaymentRadio, membershipPaymentRadio;

    @FXML
    private TextField discountCodeField; // Add this field in FXML

    @FXML
    private Button confirmPaymentButton;
    private BookingFactory bookingFactory;

    private PaymentFactory paymentFactory;
    private NotificationFactory notificationFactory;
    private MembershipFactory membershipFactory;
    private int customerid;
    boolean isDiscountApplied=false;

    public void initialize() {
        paymentFactory = new PaymentFactory();
        bookingFactory = new BookingFactory();
        membershipFactory=new MembershipFactory();
        notificationFactory = new NotificationFactory();
        setupPaymentMethods();
        setupPaymentOptions();
    }

    public void setuserId(int id) {
        this.customerid = id;
        System.out.println("Customer ID: " + customerid); // Debugging
        loadPendingPayments();
    }
    private void setupPaymentOptions() {
        bookingPaymentRadio.setOnAction(e -> loadPendingPayments());
        membershipPaymentRadio.setOnAction(e -> loadPendingMembershipPayments());
    }
    private void setupPaymentMethods() {
        paymentMethodComboBox.getItems().addAll("credit_card", "debit_card", "paypal", "cash");
        paymentMethodComboBox.setPromptText("Select Payment Method");
    }
    private void loadPendingMembershipPayments() {
        List<Payment> pendingPayments = paymentFactory.getPendingPayments(customerid);
        //populatePaymentList(pendingPayments, "No pending membership payments found.");
        paymentListContainer.getChildren().clear();

        if (pendingPayments != null && !pendingPayments.isEmpty()) {
            System.out.println("Pending Payments:");
            pendingPayments.forEach(System.out::println);

            for (Payment payment : pendingPayments) {
                // Create a row for each payment
                HBox paymentRow = new HBox(10);
                paymentRow.setStyle("-fx-padding: 10; -fx-background-color: #2d2d2d; -fx-border-color: gold; -fx-border-radius: 5; -fx-background-radius: 5;");

                Label bookingIdLabel = new Label("Membership ID: " + payment.getMembershipId());
                bookingIdLabel.setStyle("-fx-text-fill: gold; -fx-font-size: 14px;");

                Label amountLabel = new Label("Amount: $" + payment.getAmount());
                amountLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

                Label statusLabel = new Label("Status: " + payment.getStatus());
                statusLabel.setStyle("-fx-text-fill: " + ("pending".equals(payment.getStatus()) ? "red;" : "green;") + " -fx-font-size: 14px;");

                paymentRow.getChildren().addAll(bookingIdLabel, amountLabel, statusLabel);
                paymentListContainer.getChildren().add(paymentRow);
            }
        } else {
            Label noPaymentsLabel = new Label("No pending payments found.");
            noPaymentsLabel.setStyle("-fx-text-fill: gold; -fx-font-size: 14px;");
            paymentListContainer.getChildren().add(noPaymentsLabel);
        }
    }

    private void loadPendingPayments() {
        List<Payment> pendingPayments = paymentFactory.getPendingPayments(customerid);
        paymentListContainer.getChildren().clear();

        if (pendingPayments != null && !pendingPayments.isEmpty()) {
            System.out.println("Pending Payments:");
            pendingPayments.forEach(System.out::println);

            for (Payment payment : pendingPayments) {
                // Create a row for each payment
                HBox paymentRow = new HBox(10);
                paymentRow.setStyle("-fx-padding: 10; -fx-background-color: #2d2d2d; -fx-border-color: gold; -fx-border-radius: 5; -fx-background-radius: 5;");

                Label bookingIdLabel = new Label("Booking ID: " + payment.getBookingId());
                bookingIdLabel.setStyle("-fx-text-fill: gold; -fx-font-size: 14px;");

                Label amountLabel = new Label("Amount: $" + payment.getAmount());
                amountLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

                Label statusLabel = new Label("Status: " + payment.getStatus());
                statusLabel.setStyle("-fx-text-fill: " + ("pending".equals(payment.getStatus()) ? "red;" : "green;") + " -fx-font-size: 14px;");

                paymentRow.getChildren().addAll(bookingIdLabel, amountLabel, statusLabel);
                paymentListContainer.getChildren().add(paymentRow);
            }
        } else {
            Label noPaymentsLabel = new Label("No pending payments found.");
            noPaymentsLabel.setStyle("-fx-text-fill: gold; -fx-font-size: 14px;");
            paymentListContainer.getChildren().add(noPaymentsLabel);
        }
    }

    @FXML
    private void checkdiscount(){
        String discountCode = discountCodeField.getText(); // Fetch discount code from user input
        int bookingId = Integer.parseInt(bookingIdField.getText());

        if (discountCode != null && !discountCode.isEmpty()) {
            isDiscountApplied= paymentFactory.applyDiscountCode(bookingId, customerid, discountCode);
            if (!isDiscountApplied) {
                showAlert(Alert.AlertType.ERROR, "Invalid Discount", "Invalid or expired discount code.");
                return;
            }
            loadPendingPayments();
        }

    }

    @FXML
    private void handleConfirmPayment() {
        try {
            int bookingId = Integer.parseInt(bookingIdField.getText());
            double amount = Double.parseDouble(amountField.getText());
            String paymentMethod = paymentMethodComboBox.getValue();



            if (paymentMethod == null) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please select a payment method.");
                return;
            }
            // Apply discount if a code is provided


            boolean isPaymentSuccessful = paymentFactory.confirmPayment(bookingId);

            if (isPaymentSuccessful) {
                boolean isBookingCompleted;
                if (isDiscountApplied){
                     isBookingCompleted= bookingFactory.completeBooking(bookingId,amount);

                }
                else{
                    isBookingCompleted = bookingFactory.completeBooking(bookingId,amount);
                }


                if (isBookingCompleted) {
                    notificationFactory.sendNotification(customerid, "Payment confirmed for booking ID: " + bookingId, "sms");
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Payment confirmed and booking marked as completed!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Payment confirmed but failed to update booking status.");
                }
                //showAlert(Alert.AlertType.INFORMATION, "Success", "Payment confirmed successfully!");
                loadPendingPayments(); // Refresh the pending payments list
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to confirm payment. Please try again.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid values for Booking ID and Amount.");
        }
    }

    @FXML
    private void handleMembershipPayment() {
        try {
            int membershipId = Integer.parseInt(membershipIdField.getText());
            double amount = Double.parseDouble(amountField.getText());
            String paymentMethod = paymentMethodComboBox.getValue();

            if (paymentMethod == null) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please select a payment method.");
                return;
            }

            boolean isPaymentSuccessful = paymentFactory.confirmMembershipPayment(membershipId);

            if (isPaymentSuccessful) {
                boolean isMembershipActivated = membershipFactory.activateMembership(membershipId);

                if (isMembershipActivated) {
                    notificationFactory.sendNotification(customerid, "Payment confirmed for membership ID: " + membershipId, "sms");
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Membership payment confirmed and membership activated!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Payment confirmed but failed to activate membership.");
                }
                loadPendingMembershipPayments(); // Refresh the pending membership payments list
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to confirm membership payment. Please try again.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid values for Membership ID and Amount.");
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
