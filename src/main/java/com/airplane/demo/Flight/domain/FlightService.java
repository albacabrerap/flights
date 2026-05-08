package com.airplane.demo.Flight.domain;

import com.airplane.demo.Booking.Events.BookingCreatedEvent;
import com.airplane.demo.Booking.domain.Booking;
import com.airplane.demo.Booking.infraestructure.BookingRepository;
import com.airplane.demo.Flight.DTOs.*;
import com.airplane.demo.Flight.infraestructure.FlightRepository;
import com.airplane.demo.User.DTOs.NewIdDTO;
import com.airplane.demo.User.domain.User;
import com.airplane.demo.User.infraestructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ApplicationEventPublisher eventPublisher;

    public FlightDTO createFlight(NewFlightRequestDTO dto) {
        if (dto.getAirlineName() == null || dto.getAirlineName().isBlank() ||
                dto.getFlightNumber() == null || dto.getFlightNumber().isBlank() ||
                dto.getEstDepartureTime() == null || dto.getEstArrivalTime() == null ||
                dto.getAvailableSeats() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All fields are required");
        }
        if (!dto.getFlightNumber().matches("^[A-Z0-9]{1,6}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid flight number format");
        }
        if (dto.getAvailableSeats() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Available seats must be greater than 0");
        }
        if (!dto.getEstDepartureTime().isBefore(dto.getEstArrivalTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Departure time must be before arrival time");
        }
        if (flightRepository.existsByFlightNumber(dto.getFlightNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight number already exists");
        }

        Flight flight = new Flight();
        flight.setAirlineName(dto.getAirlineName());
        flight.setFlightNumber(dto.getFlightNumber());
        flight.setEstDepartureTime(dto.getEstDepartureTime());
        flight.setEstArrivalTime(dto.getEstArrivalTime());
        flight.setAvailableSeats(dto.getAvailableSeats());
        Flight saved = flightRepository.save(flight);
        return new FlightDTO(saved.getId());
    }

    @Async
    public void createManyFlights(NewFlightManyRequestDTO Flights) {
        List<NewFlightRequestDTO> flights = Flights.getFlightsRequestsDto();
        for (NewFlightRequestDTO dto : flights) {
            try {
                createFlight(dto);
            } catch (Exception e) {
                System.err.println("Could not create flight " + dto.getFlightNumber() + " - " + e.getMessage());
            }
        }
    }

    public FlightSearchResponseDTO search(String flightNumber, String airlineName,
                                          String estDepartureTimeFrom, String estDepartureTimeTo) {
        Instant from = (estDepartureTimeFrom != null && !estDepartureTimeFrom.isBlank())
                ? Instant.parse(estDepartureTimeFrom) : null;
        Instant to = (estDepartureTimeTo != null && !estDepartureTimeTo.isBlank())
                ? Instant.parse(estDepartureTimeTo) : null;

        List<FlightDTO> results = flightRepository
                .search(flightNumber, airlineName, from, to)
                .stream().map(this::toDTO).toList();

        return new FlightSearchResponseDTO(results);
    }

    @Transactional
    public NewIdDTO bookFlight(String flightId, String userEmail) {
        if (flightId == null || flightId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "flightId is required");
        }

        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (flight.getAvailableSeats() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight cannot be oversold");
        }

        Instant now = Instant.now();
        if (flight.getEstDepartureTime().isBefore(now)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight is in the past or in transit");
        }

        List<Booking> userBookings = bookingRepository.findByCustomerId(user.getId());
        List<String> bookedFlightIds = userBookings.stream()
                .map(b -> b.getFlight().getId())
                .filter(id -> !id.equals(flightId))
                .toList();

        List<Flight> bookedFlights = flightRepository.findAllById(bookedFlightIds);
        boolean hasOverlap = bookedFlights.stream().anyMatch(existing ->
                flight.getEstDepartureTime().isBefore(existing.getEstArrivalTime()) &&
                        flight.getEstArrivalTime().isAfter(existing.getEstDepartureTime()));

        if (hasOverlap) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight overlaps with an existing booking");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setCustomer(user);
        booking.setBookingDate(Instant.now());
        Booking saved = bookingRepository.save(booking);

        eventPublisher.publishEvent(new BookingCreatedEvent(saved));
        return new NewIdDTO(saved.getId());
    }

    public BookingInfoDTO getBookingById(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        return new BookingInfoDTO(
                booking.getId(),
                booking.getBookingDate(),
                booking.getFlight().getId(),
                booking.getFlight().getFlightNumber(),
                booking.getCustomer().getId(),
                booking.getCustomer().getFirstName(),
                booking.getCustomer().getLastName()
        );
    }

    public FlightDTO getFlightById(String id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found"));
        return toDTO(flight);
    }

    private FlightDTO toDTO(Flight f) {
        return new FlightDTO(f.getId(), f.getAirlineName(), f.getFlightNumber(),
                f.getEstDepartureTime(), f.getEstArrivalTime(), f.getAvailableSeats());
    }
}