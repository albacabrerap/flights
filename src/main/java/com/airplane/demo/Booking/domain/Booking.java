package com.airplane.demo.Booking.domain;

import com.airplane.demo.Flight.domain.Flight;
import com.airplane.demo.User.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Instant bookingDate;
    @ManyToOne private Flight flight;
    @ManyToOne private User customer;
}
