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

    public List<ReservationDTO> getAllReservations() {
        log.info("Obteniendo todas las reservas");
        return reservationRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public List<ReservationDTO> getAvailableReservations() {
        log.info("Consultando disponibilidad");
        return reservationRepository.findByStatus("ACTIVE").stream().map(this::mapToDTO).toList();
    }

    public Optional<ReservationDTO> getReservationById(Long id) {
        return reservationRepository.findById(id).map(this::mapToDTO);
    }

    public ReservationDTO createReservation(ReservationDTO dto) {
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

        return mapToDTO(reservationRepository.save(reservation));
    }

    public Optional<ReservationDTO> updateReservation(Long id, ReservationDTO dto) {
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
            
            return mapToDTO(reservationRepository.save(existing));
        });
    }

    public boolean deleteReservation(Long id) {
        return reservationRepository.findById(id).map(res -> {
            reservationRepository.delete(res);
            return true;
        }).orElse(false);
    }

    private ReservationDTO mapToDTO(Reservation reservation) {
        ReservationDTO dto = ReservationDTO.builder()
                .id(reservation.getId())
                .hotelId(reservation.getHotel() != null ? reservation.getHotel().getId() : null)
                .guestName(reservation.getGuestName())
                .roomType(reservation.getRoomType())
                .checkInDate(reservation.getCheckInDate())
                .checkOutDate(reservation.getCheckOutDate())
                .status(reservation.getStatus())
                .paymentMethod(reservation.getPaymentMethod())
                .build();
        return dto;
    }
}
