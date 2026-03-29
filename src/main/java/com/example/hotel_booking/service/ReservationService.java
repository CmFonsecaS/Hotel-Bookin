package com.example.hotel_booking.service;

import com.example.hotel_booking.model.Reservation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ReservationService() {
        // Inicializar registros 
        reservations.add(new Reservation(idGenerator.getAndIncrement(), "Hotel Plaza", "Carlos Perez", "Single", LocalDate.now(), LocalDate.now().plusDays(2), "ACTIVA", "TARJETA"));
        reservations.add(new Reservation(idGenerator.getAndIncrement(), "Hotel Central", "Luz Maria", "Double", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), "ACTIVA", "EFECTIVO"));
        reservations.add(new Reservation(idGenerator.getAndIncrement(), "Hotel Norte", "Juan Diaz", "Suite", LocalDate.now().plusDays(2), LocalDate.now().plusDays(5), "ACTIVA", "TRANSFERENCIA"));
        reservations.add(new Reservation(idGenerator.getAndIncrement(), "Hotel Plaza", "Ana Lopez", "Double", LocalDate.now(), LocalDate.now().plusDays(1), "CANCELADA", "TARJETA"));
        reservations.add(new Reservation(idGenerator.getAndIncrement(), "Hotel Sur", "Pedro Sanchez", "Single", LocalDate.now().plusDays(4), LocalDate.now().plusDays(6), "ACTIVA", "EFECTIVO"));
        reservations.add(new Reservation(idGenerator.getAndIncrement(), "Hotel Central", "Luisa Fernandez", "Suite", LocalDate.now().plusDays(10), LocalDate.now().plusDays(15), "ACTIVA", "TRANSFERENCIA"));
        reservations.add(new Reservation(idGenerator.getAndIncrement(), "Hotel Estelar", "Jorge Gonzalez", "Single", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), "CANCELADA", "TARJETA"));
        reservations.add(new Reservation(idGenerator.getAndIncrement(), "Hotel Norte", "Sofia Castro", "Double", LocalDate.now().plusDays(5), LocalDate.now().plusDays(8), "ACTIVA", "EFECTIVO"));
    }

    public List<Reservation> getAllReservations() {
        return reservations;
    }

    public List<Reservation> getAvailableReservations() {
        // Disponibilidad: Reservas activas
        return reservations.stream()
                .filter(res -> "ACTIVA".equals(res.getStatus()))
                .collect(Collectors.toList());
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservations.stream()
                .filter(res -> res.getId().equals(id))
                .findFirst();
    }

}
