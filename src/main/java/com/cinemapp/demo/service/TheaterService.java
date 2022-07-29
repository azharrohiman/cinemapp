package com.cinemapp.demo.service;

import com.cinemapp.demo.dto.SeatDto;
import com.cinemapp.demo.dto.TheaterDto;
import com.cinemapp.demo.dto.TicketDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TheaterService {

    private List<SeatDto> seatDtoList;

    private ConcurrentHashMap<String, SeatDto> seatMap = new ConcurrentHashMap<>();

    @Autowired
    private TokenService tokenService;

    public TheaterDto setupTheater() {
        int rows = 9;
        int columns = 9;

        seatDtoList = new ArrayList<>();

        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                var seatId = i + "" + j;
                if (i <= 4) {
                    seatDtoList.add(new SeatDto(seatId, i, j, 10, false));
                }
                else {
                    seatDtoList.add(new SeatDto(seatId, i, j, 8, false));
                }
            }
        }

        return new TheaterDto(rows, columns, seatDtoList);
    }

    public ResponseEntity purchaseSeat(SeatDto seatPurchaseInfo) {
        if (seatPurchaseInfo.getColumn() < 1 || seatPurchaseInfo.getColumn() > 9 || seatPurchaseInfo.getRow() < 1 || seatPurchaseInfo.getRow() > 9) {
            return new ResponseEntity(Map.of("error", "The number of a row or a column is out of bounds!"), HttpStatus.BAD_REQUEST);
        }

        for (var seat: seatDtoList) {
            if (seatPurchaseInfo.getRow() == seat.getRow() && seatPurchaseInfo.getColumn() == seat.getColumn()) {
                if (!seat.isSold()) {
                    var token = tokenService.generateToken();
                    seat.setSold(true);
                    seatMap.put(token, seat);

                    var ticketDto = TicketDto
                            .builder()
                            .token(token)
                            .ticket(seat)
                            .build();

                    return new ResponseEntity<>(ticketDto, HttpStatus.OK);
                }
                else {
                    return new ResponseEntity(Map.of("error", "The ticket has been already purchased!"),
                            HttpStatus.BAD_REQUEST);
                }
            }
        }

        return new ResponseEntity(Map.of("error", "The number of a row or a column is out of bounds!"), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity returnTicket(TicketDto ticketDto) {
        if (seatMap.containsKey(ticketDto.getToken())) {
            var seat = seatMap.get(ticketDto.getToken());

            seatDtoList.stream()
                    .filter(seatDto -> seatDto.getId().equals(seat.getId()))
                    .findFirst()
                    .ifPresent(seatDto -> seatDto.setSold(false));

            seatMap.remove(ticketDto.getToken());

            return new ResponseEntity(Map.of("returned_ticket", seat), HttpStatus.OK);
        }

        return new ResponseEntity(Map.of("error", "Wrong token!"), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity calculateTheaterStats() {
        Map<String, Integer> statistic = new HashMap<>();

        int currentIncome =
                seatMap.values().stream().map(SeatDto::getSeatPrice).mapToInt(Integer::intValue).sum();
        int availableSeats = seatDtoList.size() - seatMap.size();
        int numberOfPurchasedTickets = seatMap.size();

        statistic.put("current_income", currentIncome);
        statistic.put("number_of_available_seats", availableSeats);
        statistic.put("number_of_purchased_tickets", numberOfPurchasedTickets);

        return new ResponseEntity<>(statistic, HttpStatus.OK);
    }
}
