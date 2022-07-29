package com.cinemapp.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TheaterDto {

    @JsonProperty("total_rows")
    private int rows;

    @JsonProperty("total_columns")
    private int columns;

    @JsonProperty("available_seats")
    private List<SeatDto> seatDtoList;
}
