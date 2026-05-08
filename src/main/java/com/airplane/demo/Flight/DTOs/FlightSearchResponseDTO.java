package com.airplane.demo.Flight.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightSearchResponseDTO {
    private List<FlightDTO> items;

}

