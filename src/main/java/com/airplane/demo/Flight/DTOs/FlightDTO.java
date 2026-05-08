package com.airplane.demo.Flight.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {
    private String id;
    private String airlineName;
    private String flightNumber;
    private Instant estDepartureTime;
    private Instant estArrivalTime;
    private Integer availableSeats;

    public FlightDTO(String id_){
        id=id_;
    }
}
