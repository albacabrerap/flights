package com.airplane.demo.Flight.infraestructure;

import com.airplane.demo.Flight.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, String> {

    boolean existsByFlightNumber(String flightNumber);

    @Query("SELECT f FROM Flight f WHERE " +
            "(:flightNumber IS NULL OR f.flightNumber LIKE %:flightNumber%) AND " +
            "(:airlineName IS NULL OR f.airlineName LIKE %:airlineName%) AND " +
            "(:from IS NULL OR f.estDepartureTime >= :from) AND " +
            "(:to IS NULL OR f.estDepartureTime <= :to)")
    List<Flight> search(@Param("flightNumber") String flightNumber,
                        @Param("airlineName") String airlineName,
                        @Param("from") Instant from,
                        @Param("to") Instant to);
}
