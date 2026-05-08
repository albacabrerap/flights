package com.airplane.demo.Flight.domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Flight {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @NotBlank private String airlineName;
    @NotBlank @Pattern(regexp = "^[A-Z0-9]{1,6}$") private String flightNumber;
    private Instant estDepartureTime;
    private Instant estArrivalTime;
    private Integer availableSeats;
}