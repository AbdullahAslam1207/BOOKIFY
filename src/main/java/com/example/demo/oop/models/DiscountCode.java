package com.example.demo.oop.models;

import java.sql.Date;

public class DiscountCode {
    private int id;
    private int userId;
    private String code;
    private int discountPercentage;
    private Date validity;
    private boolean isUsed;

    // Constructor
    public DiscountCode(int id, int userId, String code, int discountPercentage, Date validity, boolean isUsed) {
        this.id = id;
        this.userId = userId;
        this.code = code;
        this.discountPercentage = discountPercentage;
        this.validity = validity;
        this.isUsed = isUsed;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Date getValidity() {
        return validity;
    }

    public void setValidity(Date validity) {
        this.validity = validity;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    public String toString() {
        return "DiscountCode{" +
                "id=" + id +
                ", userId=" + userId +
                ", code='" + code + '\'' +
                ", discountPercentage=" + discountPercentage +
                ", validity=" + validity +
                ", isUsed=" + isUsed +
                '}';
    }
}
