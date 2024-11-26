package com.example.demo.oop.factories;

import com.example.demo.database.queries.BookingQueries;
import com.example.demo.oop.models.Booking;

import java.sql.Date;

public class BookingFactory {
    private final BookingQueries bookingQueries;

    public BookingFactory() {
        this.bookingQueries = new BookingQueries();
    }

    public Booking createBooking(int userId, int serviceId, java.sql.Date bookingDate, double price, int roomId, int hotelId) {
        int bookingId = bookingQueries.insertBooking(userId, serviceId, bookingDate, price);
        if (bookingId > 0) {
            boolean roomBookingSuccess = bookingQueries.insertRoomBooking(bookingId, roomId, hotelId, userId);
            if (roomBookingSuccess) {
                return new Booking(bookingId, userId, serviceId, bookingDate, "pending", price);
            }
        }
        return null;
    }
    public boolean completeBooking(int bookingId, double amount) {
        return bookingQueries.updateBookingStatus(bookingId, "completed", amount);
    }

    public Booking createTableBooking(int userId, int serviceId, java.sql.Date bookingDate, double price, int tableId, int restaurantId) {
        int bookingId = bookingQueries.insertBooking(userId, serviceId, bookingDate, price);
        if (bookingId > 0) {
            boolean tableBookingSuccess = bookingQueries.insertTableBooking(bookingId, tableId, restaurantId, userId);
            if (tableBookingSuccess) {
                return new Booking(bookingId, userId, serviceId, bookingDate, "pending", price);
            }
        }
        return null; // Return null if booking creation fails
    }



}
