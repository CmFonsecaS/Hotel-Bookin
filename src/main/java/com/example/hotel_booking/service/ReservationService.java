package com.example.hotel_booking.service;

import com.example.hotel_booking.dto.ReservationDTO;
import com.example.hotel_booking.model.Hotel;
import com.example.hotel_booking.model.Reservation;
import com.example.hotel_booking.repository.HotelRepository;
import com.example.hotel_booking.repository.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final HotelRepository hotelRepository;

    public ReservationService(ReservationRepository reservationRepository, HotelRepository hotelRepository) {
        this.reservationRepository = reservationRepository;
        this.hotelRepository = hotelRepository;
    }

    public List<Reservation> getAllReservations() {
        log.info("Obteniendo todas las reservas");
        return reservationRepository.findAll();
    }

    public List<Reservation> getAvailableReservations() {
        log.info("Consultando disponibilidad");
        return reservationRepository.findByStatus("ACTIVE");
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    public Reservation createReservation(ReservationDTO dto) {
        log.info("Creando reserva desde DTO para: {}", dto.getGuestName());
        
        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado con ID: " + dto.getHotelId()));

        Reservation reservation = Reservation.builder()
                .hotel(hotel)
                .guestName(dto.getGuestName())
                .roomType(dto.getRoomType())
                .checkInDate(dto.getCheckInDate())
                .checkOutDate(dto.getCheckOutDate())
                .status(dto.getStatus() != null ? dto.getStatus() : "ACTIVE")
                .paymentMethod(dto.getPaymentMethod())
                .build();

        return reservationRepository.save(reservation);
    }

    public Optional<Reservation> updateReservation(Long id, ReservationDTO dto) {
        log.info("Actualizando reserva ID: {} desde DTO", id);
        
        return reservationRepository.findById(id).map(existing -> {
            Hotel hotel = hotelRepository.findById(dto.getHotelId())
                    .orElseThrow(() -> new RuntimeException("Hotel no encontrado con ID: " + dto.getHotelId()));
            
            existing.setHotel(hotel);
            existing.setGuestName(dto.getGuestName());
            existing.setRoomType(dto.getRoomType());
            existing.setCheckInDate(dto.getCheckInDate());
            existing.setCheckOutDate(dto.getCheckOutDate());
            existing.setStatus(dto.getStatus());
            existing.setPaymentMethod(dto.getPaymentMethod());
            
            return reservationRepository.save(existing);
        });
    }

    public boolean deleteReservation(Long id) {
        return reservationRepository.findById(id).map(res -> {
            reservationRepository.delete(res);
            return true;
        }).orElse(false);
    }
}
