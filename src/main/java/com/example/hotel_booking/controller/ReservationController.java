package com.example.hotel_booking.controller;

import com.example.hotel_booking.dto.ReservationDTO;
import com.example.hotel_booking.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public CollectionModel<EntityModel<ReservationDTO>> getAllReservations() {
        List<EntityModel<ReservationDTO>> reservations = reservationService.getAllReservations().stream()
                .map(this::addLinks)
                .collect(Collectors.toList());

        return CollectionModel.of(reservations,
                linkTo(methodOn(ReservationController.class).getAllReservations()).withSelfRel());
    }

    @GetMapping("/availability")
    public CollectionModel<EntityModel<ReservationDTO>> getAvailableReservations() {
        List<EntityModel<ReservationDTO>> reservations = reservationService.getAvailableReservations().stream()
                .map(this::addLinks)
                .collect(Collectors.toList());

        return CollectionModel.of(reservations,
                linkTo(methodOn(ReservationController.class).getAvailableReservations()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ReservationDTO>> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(this::addLinks)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EntityModel<ReservationDTO>> createReservation(@Valid @RequestBody ReservationDTO reservationDTO) {
        ReservationDTO createdReservation = reservationService.createReservation(reservationDTO);
        return new ResponseEntity<>(addLinks(createdReservation), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ReservationDTO>> updateReservation(@PathVariable Long id, @Valid @RequestBody ReservationDTO reservationDTO) {
        return reservationService.updateReservation(id, reservationDTO)
                .map(this::addLinks)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        if (reservationService.deleteReservation(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private EntityModel<ReservationDTO> addLinks(ReservationDTO dto) {
        EntityModel<ReservationDTO> entityModel = EntityModel.of(dto);
        entityModel.add(linkTo(methodOn(ReservationController.class).getReservationById(dto.getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(ReservationController.class).getAllReservations()).withRel("all-reservations"));
        return entityModel;
    }
}
