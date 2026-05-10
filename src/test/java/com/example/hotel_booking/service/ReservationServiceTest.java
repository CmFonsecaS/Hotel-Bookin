package com.example.hotel_booking.service;

import com.example.hotel_booking.dto.ReservationDTO;
import com.example.hotel_booking.model.Hotel;
import com.example.hotel_booking.model.Reservation;
import com.example.hotel_booking.repository.HotelRepository;
import com.example.hotel_booking.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Hotel sampleHotel;
    private Reservation sampleReservation;
    private ReservationDTO sampleDTO;

    @BeforeEach
    void setUp() {
        sampleHotel = new Hotel();
        sampleHotel.setId(1L);
        sampleHotel.setName("Hotel Test");

        sampleReservation = Reservation.builder()
                .id(1L)
                .hotel(sampleHotel)
                .guestName("John Doe")
                .roomType("Suite")
                .checkInDate(LocalDate.now().plusDays(1))
                .checkOutDate(LocalDate.now().plusDays(5))
                .status("ACTIVE")
                .paymentMethod("CARD")
                .build();

        sampleDTO = ReservationDTO.builder()
                .hotelId(1L)
                .guestName("John Doe")
                .roomType("Suite")
                .checkInDate(LocalDate.now().plusDays(1))
                .checkOutDate(LocalDate.now().plusDays(5))
                .status("ACTIVE")
                .paymentMethod("CARD")
                .build();
    }

    @Test
    void testGetAllReservations() {
        // Arrange
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(sampleReservation));

        // Act
        List<ReservationDTO> result = reservationService.getAllReservations();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getGuestName());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testCreateReservation() {
        // Arrange
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(sampleHotel));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(sampleReservation);

        // Act
        ReservationDTO createdDTO = reservationService.createReservation(sampleDTO);

        // Assert
        assertNotNull(createdDTO);
        assertEquals(1L, createdDTO.getId());
        assertEquals("John Doe", createdDTO.getGuestName());
        verify(hotelRepository, times(1)).findById(1L);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testGetReservationById() {
        // Arrange
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(sampleReservation));

        // Act
        Optional<ReservationDTO> result = reservationService.getReservationById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getGuestName());
        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteReservation() {
        // Arrange
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(sampleReservation));
        doNothing().when(reservationRepository).delete(sampleReservation);

        // Act
        boolean result = reservationService.deleteReservation(1L);

        // Assert
        assertTrue(result);
        verify(reservationRepository, times(1)).findById(1L);
        verify(reservationRepository, times(1)).delete(sampleReservation);
    }
}
