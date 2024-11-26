package com.example.demo.controllers;

import com.example.demo.database.queries.DiscountCodeQueries;
import com.example.demo.database.queries.UserQueries;
import com.example.demo.oop.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class TargetedOffersController {

    @FXML
    private ComboBox<Integer> discountComboBox;

    @FXML
    private DatePicker validityDatePicker;

    @FXML
    private TextField discountCodeField;

    @FXML
    private ListView<User> usersListView;

    @FXML
    private Button createOfferButton;

    private DiscountCodeQueries discountCodeQueries;
    private UserQueries userQueries;

    public void initialize() {
        discountCodeQueries = new DiscountCodeQueries();
        userQueries = new UserQueries(); // Initialize UserQueries to fetch users

        // Populate the discount options (e.g., percentages)
        discountComboBox.getItems().addAll(5, 10, 15, 20, 25, 30);

        // Populate the usersListView
        populateUsersList();

        // Set up the ListView for multi-selection
        usersListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Set event handler for Create Offer button
        createOfferButton.setOnAction(event -> handleCreateOffer());
    }

    private void populateUsersList() {
        try {
            // Fetch users from the database
            List<User> users = userQueries.getAllCustomers();

            // Convert the list of users to an ObservableList
            ObservableList<User> observableUsers = FXCollections.observableArrayList(users);

            // Set the items for the usersListView
            usersListView.setItems(observableUsers);

            // Optionally, set a custom cell factory to display user details
            usersListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(User user, boolean empty) {
                    super.updateItem(user, empty);
                    if (empty || user == null) {
                        setText(null);
                    } else {
                        setText(user.getName() + " (ID: " + user.getId() + ")");
                    }
                }
            });

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch users: " + e.getMessage());
        }
    }

    private void handleCreateOffer() {
        Integer discount = discountComboBox.getValue();
        LocalDate validityDate = validityDatePicker.getValue();
        String discountCode = discountCodeField.getText();
        ObservableList<User> selectedUsers = usersListView.getSelectionModel().getSelectedItems();

        // Validate inputs
        if (discount == null || validityDate == null || discountCode == null || selectedUsers.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing Input", "Please fill all fields and select users.");
            return;
        }

        for (User user : selectedUsers) {
            boolean isCreated = discountCodeQueries.createDiscountCode(
                    user.getId(),
                    discountCode,
                    discount,
                    String.valueOf(Date.valueOf(validityDate)) // Convert LocalDate to SQL Date
            );

            if (!isCreated) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to create discount code for user: " + user.getName());
            }
        }

        showAlert(Alert.AlertType.INFORMATION, "Success", "Discount codes created successfully!");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
