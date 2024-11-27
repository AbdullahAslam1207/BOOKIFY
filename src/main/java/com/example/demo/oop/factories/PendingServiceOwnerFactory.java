package com.example.demo.oop.factories;

import com.example.demo.database.queries.UserQueries;
import com.example.demo.oop.models.User;

import java.util.List;

public class PendingServiceOwnerFactory {
    private final UserQueries userQueries;

    public PendingServiceOwnerFactory() {
        this.userQueries = new UserQueries();
    }

    public boolean addPendingServiceOwner(String name, String email, String password, String phoneNumber) {
        return userQueries.insertPendingServiceOwner(name, email, password, phoneNumber) > 0;
    }

    public List<User> getAllPendingServiceOwners() {
        return userQueries.getPendingServiceOwners();
    }

    public boolean approveServiceOwner(int pendingId) {
        return userQueries.approveServiceOwner(pendingId);
    }

    public boolean disapproveServiceOwner(int pendingId) {
        return userQueries.disapproveServiceOwner(pendingId);
    }
}
