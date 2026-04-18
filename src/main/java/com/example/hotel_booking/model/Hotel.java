package com.example.hotel_booking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "HOTELS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del hotel es obligatorio")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 100)
    private String location;

    @Size(max = 50)
    private String category;
}
