package com.example.demo.oop.models;

import java.sql.Date;
import java.sql.Time;

public class Notification {
    private int id;
    private int userId;
    private String description;
    private Date date;
    private Time time;
    private String type;
    private String status;

    public Notification(int id, int userId, String description, Date date, Time time, String type, String status) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.date = date;
        this.time = time;
        this.type = type;
        this.status = status;
    }

    // Getters and setters
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
