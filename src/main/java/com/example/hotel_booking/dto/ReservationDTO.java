package com.example.hotel_booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDTO extends RepresentationModel<ReservationDTO> {

    private Long id;

    @NotNull(message = "El ID del hotel es obligatorio")
    private Long hotelId;

    @NotBlank(message = "El nombre del huésped es obligatorio")
    @Size(max = 100, message = "El nombre del huésped no debe exceder los 100 caracteres")
    private String guestName;

    @NotBlank(message = "El tipo de habitación es obligatorio")
    @Size(max = 50, message = "el tipo de habitación no debe exceder los 50 caracteres")
    private String roomType;

    @NotNull(message = "La fecha de check-in es obligatoria")
    @FutureOrPresent(message = "La fecha de check-in no puede ser en el pasado")
    private LocalDate checkInDate;

    @NotNull(message = "La fecha de check-out es obligatoria")
    private LocalDate checkOutDate;

    @Size(max = 20)
    private String status;

    @Size(max = 20)
    private String paymentMethod;
}
