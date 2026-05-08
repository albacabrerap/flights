package com.airplane.demo.cleanup;

import com.airplane.demo.Booking.infraestructure.BookingRepository;
import com.airplane.demo.Flight.infraestructure.FlightRepository;
import com.airplane.demo.User.infraestructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cleanup")
public class CleanupController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private UserRepository userRepository;

    @DeleteMapping
    public ResponseEntity<Void> cleanup() {

        bookingRepository.deleteAll();
        flightRepository.deleteAll();
        userRepository.deleteAll();
        return ResponseEntity.ok().build();
    }
}
