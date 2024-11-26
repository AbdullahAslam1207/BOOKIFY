package com.example.demo.oop.models;

import java.util.Date;

public class Booking {
    private int id;
    private int userId;
    private int serviceId;
    private Date bookingDate;
    private String status;
    private double price;

    public Booking(int id, int userId, int serviceId, Date bookingDate, String status, double price) {
        this.id = id;
        this.userId = userId;
        this.serviceId = serviceId;
        this.bookingDate = bookingDate;
        this.status = status;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public double getPrice() {
        return price;
    }

}
