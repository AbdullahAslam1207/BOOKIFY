package com.example.demo.controllers;

import com.example.demo.oop.factories.MembershipFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class MembershipController {
    private MembershipFactory membershipFactory;
    private int userId;

    public void initialize() {
        membershipFactory = new MembershipFactory();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @FXML
    private void handleSimpleMembership() {
        purchaseMembership(userId,"Simple",1000,"cash");
    }

    @FXML
    private void handleDeluxeMembership() {
        purchaseMembership(userId,"Deluxe",2000,"cash");
    }

    @FXML
    private void handleExclusiveMembership() {
        purchaseMembership(userId,"Exclusive",3000,"cash");
    }

    private void purchaseMembership(int userId, String type, double price, String paymentMethod) {
        boolean success = membershipFactory.purchaseMembership(userId, type, price, paymentMethod);
        if (success) {
            showAlert("Membership purchased successfully!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Failed to purchase membership.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
