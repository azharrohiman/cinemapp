package com.cinemapp.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketDto {

    private String token;

    private SeatDto ticket;
}
