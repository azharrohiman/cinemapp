package com.cinemapp.demo.endpoint;

import com.cinemapp.demo.dto.SeatDto;
import com.cinemapp.demo.dto.TheaterDto;
import com.cinemapp.demo.dto.TicketDto;
import com.cinemapp.demo.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TheaterEndpoint {

    @Autowired
    private TheaterService theaterService;

    @GetMapping("/seats")
    public TheaterDto getTheaterSeats() {
        return theaterService.setupTheater();
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseSeat(@RequestBody SeatDto seat) {
        return theaterService.purchaseSeat(seat);
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnTicket(@RequestBody TicketDto ticketDto) {
        return theaterService.returnTicket(ticketDto);
    }

    @PostMapping("/stats")
    public ResponseEntity<?> calculateStats(@RequestParam(value = "password", required = false) String password) {
        if (password != null && password.equals("super_secret")) {
            return theaterService.calculateTheaterStats();
        }
        else {
            return new ResponseEntity(Map.of("error", "The password is wrong!"), HttpStatus.valueOf(401));
        }
    }
}