package tn.esprit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.services.IVehiculeService;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
@Tag(name = "Véhicules", description = "CRUD des véhicules de location")
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    public VehiculeController(IVehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    @PostMapping
    @Operation(summary = "Ajouter un véhicule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Véhicule créé"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    public ResponseEntity<Vehicule> addVehicule(@RequestBody(required = false) Vehicule vehicule) {
        if (vehicule == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.status(201).body(vehiculeService.save(vehicule));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Afficher tous les véhicules")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des véhicules retournée")
    })
    public ResponseEntity<List<Vehicule>> getAllVehicules() {
        return ResponseEntity.ok(vehiculeService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Afficher un véhicule par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Véhicule trouvé"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    public ResponseEntity<Vehicule> getVehiculeById(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Vehicule vehicule = vehiculeService.findById(id);
        if (vehicule == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vehicule);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un véhicule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Véhicule modifié"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    public ResponseEntity<Vehicule> updateVehicule(@PathVariable Long id,
                                                    @RequestBody(required = false) Vehicule vehicule) {
        if (id <= 0 || vehicule == null) {
            return ResponseEntity.badRequest().build();
        }
        if (vehiculeService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        vehicule.setIdVehicule(id);
        try {
            return ResponseEntity.ok(vehiculeService.save(vehicule));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un véhicule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Véhicule supprimé"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide ou véhicule référencé"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    public ResponseEntity<Void> deleteVehicule(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        if (vehiculeService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            vehiculeService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/affecterEquipement/{idVehicule}/{idEquipement}")
    @Operation(summary = "Affecter un équipement à un véhicule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Équipement affecté au véhicule"),
            @ApiResponse(responseCode = "400", description = "Identifiants invalides"),
            @ApiResponse(responseCode = "404", description = "Véhicule ou équipement introuvable")
    })
    public ResponseEntity<Vehicule> affecterEquipement(@PathVariable Long idVehicule,
                                                       @PathVariable Long idEquipement) {
        if (idVehicule == null || idVehicule <= 0 || idEquipement == null || idEquipement <= 0) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.ok(vehiculeService.affecterEquipement(idVehicule, idEquipement));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/desaffecterEquipement/{idVehicule}/{idEquipement}")
    @Operation(summary = "Désaffecter un équipement d'un véhicule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Équipement désaffecté du véhicule"),
            @ApiResponse(responseCode = "400", description = "Identifiants invalides"),
            @ApiResponse(responseCode = "404", description = "Véhicule ou équipement introuvable")
    })
    public ResponseEntity<Vehicule> desaffecterEquipement(@PathVariable Long idVehicule,
                                                          @PathVariable Long idEquipement) {
        if (idVehicule == null || idVehicule <= 0 || idEquipement == null || idEquipement <= 0) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.ok(vehiculeService.desaffecterEquipement(idVehicule, idEquipement));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
