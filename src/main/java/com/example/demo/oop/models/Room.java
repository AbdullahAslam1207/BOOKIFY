package com.example.demo.oop.models;

public class Room {
    private int id;
    private int roomNumber;
    private double price;
    private int numberOfBeds;
    private String type;
    private String availability;
    private int hotelid;

    public Room(int id, int roomNumber, double price, int numberOfBeds, String type, String availability) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.price = price;
        this.numberOfBeds = numberOfBeds;
        this.type = type;
        this.availability = availability;
    }

    public Room(int id, int hotelid ,int roomNumber, double price, int numberOfBeds, String type, String availability) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.price = price;
        this.numberOfBeds = numberOfBeds;
        this.type = type;
        this.availability = availability;
        this.hotelid=hotelid;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public double getPrice() {
        return price;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public String getType() {
        return type;
    }

    public String getAvailability() {
        return availability;
    }
}
