package com.airplane.demo.Booking.Events;

import com.airplane.demo.Booking.domain.Booking;
import com.airplane.demo.Flight.domain.Flight;
import com.airplane.demo.User.domain.User;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class BookingEventListener {

    @EventListener
    public void handleBookingCreated(BookingCreatedEvent event) {
        Booking b = event.getBooking();
        Flight f = b.getFlight();
        User u = b.getCustomer();

        String content = """
            Hello %s %s,

            Your booking was successful!

            The booking is for flight %s with departure date of %s and arrival date of %s.

            The booking was registered at %s.

            Bon Voyage!
            Fly Away Travel
            """.formatted(
                u.getFirstName(),
                u.getLastName(),
                f.getFlightNumber(),
                f.getEstDepartureTime().toString(),
                f.getEstArrivalTime().toString(),
                b.getBookingDate().toString()
        );

        try {
            String filename = "flight_booking_email_" + b.getId() + ".txt";
            Files.writeString(Path.of(filename), content);
        } catch (IOException e) {
            System.err.println("Failed to write email file: " + e.getMessage());
        }
    }
}


