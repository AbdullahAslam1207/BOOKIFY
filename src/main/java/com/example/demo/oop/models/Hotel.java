package com.example.demo.oop.models;

import java.util.ArrayList;
import java.util.List;

public class Hotel extends Service {
    private int totalRooms;
    private List<Room> rooms;
    private int hotelid;

    public Hotel(int hotelid,int id, String name, String location, int totalRooms) {
        super(id, name);
        this.hotelid=hotelid;
        this.totalRooms = totalRooms;
        this.rooms = new ArrayList<>();
    }

    // Add room to the hotel
    public void addRoom(Room room) {
        rooms.add(room);
    }

    @Override
    public String getServiceType() {
        return "Hotel";
    }

    // Getters and setters
    public int getHotelid() {
        return hotelid;
    }

    public List<Room> getRooms() {
        return rooms;
    }
}
