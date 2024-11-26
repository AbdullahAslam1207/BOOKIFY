package com.example.demo.oop.models;

import java.util.ArrayList;
import java.util.List;

public class ServiceProvider {
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private List<Service> services;

    public ServiceProvider(int id, String name, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.services = new ArrayList<>();
    }

    // Add service to the provider
    public void addService(Service service) {
        services.add(service);
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Service> getServices() {
        return services;
    }
}
