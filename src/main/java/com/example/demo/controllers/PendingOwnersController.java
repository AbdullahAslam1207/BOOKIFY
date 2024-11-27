package com.example.demo.controllers;

import com.example.demo.oop.factories.PendingServiceOwnerFactory;
import com.example.demo.oop.models.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class PendingOwnersController {

    @FXML
    private TableView<User> pendingOwnersTable;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> phoneColumn;

    @FXML
    private Button approveButton, disapproveButton;

    private PendingServiceOwnerFactory pendingServiceOwnerFactory;

    public void initialize() {
        // Initialize the factory
        pendingServiceOwnerFactory = new PendingServiceOwnerFactory();

        // Set up the table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        // Load pending service owners
        loadPendingOwners();
    }

    private void loadPendingOwners() {
        // Fetch pending service owners
        List<User> pendingOwners = pendingServiceOwnerFactory.getAllPendingServiceOwners();

        // Bind data to the table
        pendingOwnersTable.setItems(FXCollections.observableArrayList(pendingOwners));
    }

    @FXML
    private void handleApproveAction() {
        User selectedOwner = pendingOwnersTable.getSelectionModel().getSelectedItem();
        if (selectedOwner == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a service owner to approve.");
            return;
        }

        boolean isApproved = pendingServiceOwnerFactory.approveServiceOwner(selectedOwner.getId());
        if (isApproved) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Service owner approved successfully.");
            loadPendingOwners();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to approve the service owner.");
        }
    }

    @FXML
    private void handleDisapproveAction() {
        User selectedOwner = pendingOwnersTable.getSelectionModel().getSelectedItem();
        if (selectedOwner == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a service owner to disapprove.");
            return;
        }

        boolean isDisapproved = pendingServiceOwnerFactory.disapproveServiceOwner(selectedOwner.getId());
        if (isDisapproved) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Service owner disapproved successfully.");
            loadPendingOwners();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to disapprove the service owner.");
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
