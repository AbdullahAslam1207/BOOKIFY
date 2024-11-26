package com.example.demo.oop.factories;

import com.example.demo.oop.models.Room;
import com.example.demo.database.queries.RoomQueries;

import java.util.Date;
import java.util.List;

public class RoomFactory {
    private final RoomQueries roomQueries;

    public RoomFactory() {
        this.roomQueries = new RoomQueries();
    }

    // Method to create a new room
    public Room createRoom(int roomNumber, double price, int numberOfBeds, String type, String availability, int hotelId) {
        int roomId = roomQueries.insertRoom(roomNumber, price, numberOfBeds, type, availability, hotelId);
        if (roomId > 0) {
            return new Room(roomId, roomNumber, price, numberOfBeds, type, availability);
        }
        return null;
    }
    public List<Room> getAvailableRoomsByHotel(int hotelId, Date bookingDate) {
        return roomQueries.fetchAvailableRoomsByHotel(hotelId, bookingDate);
    }
    public Room getRoomById(int roomId, int hotelId) {
        return roomQueries.fetchRoomByIdAndHotelId(roomId, hotelId);
    }
    public boolean updateRoomAvailability(int roomId, int hotelId, String availability) {
        return roomQueries.updateRoomAvailability(roomId, hotelId, availability);
    }



}
