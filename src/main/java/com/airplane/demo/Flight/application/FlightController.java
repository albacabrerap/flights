package com.airplane.demo.Flight.application;


import com.airplane.demo.Flight.DTOs.*;
import com.airplane.demo.Flight.domain.Flight;
import com.airplane.demo.Flight.domain.FlightService;
import com.airplane.demo.Flight.infraestructure.FlightRepository;
import com.airplane.demo.User.DTOs.NewIdDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @PostMapping("/create")
    public ResponseEntity<FlightDTO> create(@RequestBody NewFlightRequestDTO dto) {
        FlightDTO result = flightService.createFlight(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    @PostMapping("/create-many")
    public ResponseEntity<Void> createMany(@RequestBody NewFlightManyRequestDTO dto) {
        flightService.createManyFlights(dto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/search")
    public ResponseEntity<FlightSearchResponseDTO> search(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String airlineName,
            @RequestParam(required = false) String estDepartureTimeFrom,
            @RequestParam(required = false) String estDepartureTimeTo) {

        FlightSearchResponseDTO result = flightService.search(
                flightNumber, airlineName, estDepartureTimeFrom, estDepartureTimeTo
        );
        return ResponseEntity.ok(result);
    }

    @PostMapping("/book")
    public ResponseEntity<NewIdDTO> book(@RequestBody FlightBookRequestDTO dto,
                                         Authentication authentication) {
        String email = authentication.getName();
        NewIdDTO result = flightService.bookFlight(dto.getFlightId(), email);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookingInfoDTO> getBooking(@PathVariable String id) {
        BookingInfoDTO result = flightService.getBookingById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDTO> getById(@PathVariable String id) {
        FlightDTO result = flightService.getFlightById(id);
        return ResponseEntity.ok(result);
    }




}