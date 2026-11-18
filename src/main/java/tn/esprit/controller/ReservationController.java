package tn.esprit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.services.IReservationService;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Réservations", description = "CRUD des réservations")
public class ReservationController {

    private final IReservationService reservationService;

    public ReservationController(IReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @Operation(summary = "Ajouter une réservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Réservation créée"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    public ResponseEntity<Reservation> addReservation(@RequestBody(required = false) Reservation reservation) {
        if (reservation == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.status(201).body(reservationService.save(reservation));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Afficher toutes les réservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des réservations retournée")
    })
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Afficher une réservation par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Réservation trouvée"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Reservation reservation = reservationService.findById(id);
        if (reservation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reservation);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une réservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Réservation modifiée"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id,
                                                          @RequestBody(required = false) Reservation reservation) {
        if (id <= 0 || reservation == null) {
            return ResponseEntity.badRequest().build();
        }
        if (reservationService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        reservation.setIdReservation(id);
        try {
            return ResponseEntity.ok(reservationService.save(reservation));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une réservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Réservation supprimée"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide ou réservation référencée"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        if (reservationService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            reservationService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
