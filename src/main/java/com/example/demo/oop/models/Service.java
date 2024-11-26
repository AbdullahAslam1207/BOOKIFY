package com.example.demo.oop.models;

public abstract class Service {
    private int id;
    private String name;


    public Service(int id, String name) {
        this.id = id;
        this.name = name;

    }

    // Abstract method for service-specific details
    public abstract String getServiceType();

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


}
