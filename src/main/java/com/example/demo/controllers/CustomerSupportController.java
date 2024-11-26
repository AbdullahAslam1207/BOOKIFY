package com.example.demo.controllers;

import com.example.demo.database.queries.HelpQueries;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;


import java.util.List;

public class CustomerSupportController {

    @FXML
    private TextArea supportMessages;

    private HelpQueries helpQueries;
    private int userId;

    public void initialize() {
        helpQueries = new HelpQueries();
    }

    public void setUserId(int userId) {
        this.userId = userId;
        loadChatHistory();
    }

    private void loadChatHistory() {
        List<String> chatHistory = helpQueries.fetchChatHistoryByUserId(userId);
        supportMessages.clear();
        for (String message : chatHistory) {
            supportMessages.appendText(message + "\n");
        }
    }

    @FXML
    private void sendMessage() {
        String message = supportMessages.getText().trim();
        if (message.isEmpty()) {
            System.out.println("No message entered.");
            return;
        }

        boolean success = helpQueries.addMessage(userId, message, "customer");
        if (success) {
            supportMessages.appendText("You: " + message + "\n");
            supportMessages.clear();
        } else {
            System.out.println("Failed to send message.");
        }
    }
}
