package com.airplane.demo.Booking.Events;

import com.airplane.demo.Booking.domain.Booking;
import lombok.Getter;

@Getter
public class BookingCreatedEvent {
    private final Booking booking;

    public BookingCreatedEvent(Booking booking) {
        this.booking = booking;
    }

}
