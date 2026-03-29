package com.example.hotel_booking.controller;

import com.example.hotel_booking.model.Reservation;
import com.example.hotel_booking.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Consultar disponibilidad
    @GetMapping("/availability")
    public ResponseEntity<List<Reservation>> getAvailableReservations() {
        return ResponseEntity.ok(reservationService.getAvailableReservations());
    }

    // Obtener todas las reservas
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    // Buscar una reserva por ID
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
