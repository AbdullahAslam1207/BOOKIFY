package com.example.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import com.example.demo.database.queries.HelpQueries;

import java.util.List;
import java.util.Arrays;

public class CustomerSupportAdminController {

    @FXML
    private TextArea supportMessages;

    private HelpQueries helpQueries;

    public void initialize() {
        helpQueries = new HelpQueries();
        loadPendingIssues();
    }

    private void loadPendingIssues() {
        List<String> issues = helpQueries.fetchPendingIssues();
        supportMessages.clear();
        for (String issue : issues) {
            supportMessages.appendText(issue + "\n");
        }
    }

    @FXML
    private void sendMessage() {
        String[] lines = supportMessages.getText().split("\n");
        if (lines.length < 2) {
            System.out.println("Invalid format. Make sure to select a user and include a message.");
            return;
        }

        int userId = Integer.parseInt(lines[0].replace("User ID: ", "").trim());


        String message = String.join("\n", Arrays.copyOfRange(lines, 1, lines.length)).trim();

        boolean success = helpQueries.addMessage(userId, message, "admin");
        if (success) {
            System.out.println("Message sent.");
        } else {
            System.out.println("Failed to send message.");
        }
    }
}
