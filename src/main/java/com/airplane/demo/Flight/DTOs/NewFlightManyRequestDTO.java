package com.airplane.demo.Flight.DTOs;


import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class NewFlightManyRequestDTO {
    private List<NewFlightRequestDTO> FlightsRequestsDto;
}
