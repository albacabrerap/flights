package com.airplane.demo.Booking.infraestructure;

import com.airplane.demo.Booking.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    List<Booking> findByCustomerId(String customerId);
}