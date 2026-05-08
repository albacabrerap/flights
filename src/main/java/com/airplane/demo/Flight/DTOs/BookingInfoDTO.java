package com.airplane.demo.Flight.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingInfoDTO {
    private String id;
    private Instant bookingDate;
    private String flightId;
    private String flightNumber;
    private String customerId;
    private String customerFirstName;
    private String customerLastName;
}