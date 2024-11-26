package com.example.demo.oop.models;

import java.sql.Date;

public class Membership {
    private int id;
    private int userId;
    private String type; // Simple, Deluxe, or Exclusive
    private int discountPercentage;
    private String status; // active or inactive
    private Date startDate;
    private Date endDate;

    public Membership(int id, int userId, String type, int discountPercentage, String status, Date startDate, Date endDate) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.discountPercentage = discountPercentage;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public Membership(){

    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    // Method to calculate the discount for a given amount
    public double calculateDiscount(double amount) {
        if ("active".equalsIgnoreCase(this.status)) {
            return (amount * this.discountPercentage) / 100;
        }
        return 0.0;
    }

    @Override
    public String toString() {
        return "Membership{" +
                "id=" + id +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", discountPercentage=" + discountPercentage +
                ", status='" + status + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
