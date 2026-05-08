package com.airplane.demo.Flight.DTOs;

import lombok.Data;


import java.time.Instant;

@Data

public class NewFlightRequestDTO {
    private String airlineName;
    private String flightNumber;
    private Instant estDepartureTime;
    private Instant estArrivalTime;
    private Integer availableSeats;
}
