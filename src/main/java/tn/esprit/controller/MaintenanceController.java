package tn.esprit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.services.IMaintenanceService;

import java.util.List;

@RestController
@RequestMapping("/api/maintenances")
@Tag(name = "Maintenances", description = "CRUD des maintenances des véhicules")
public class MaintenanceController {

    private final IMaintenanceService maintenanceService;

    public MaintenanceController(IMaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @PostMapping
    @Operation(summary = "Ajouter une maintenance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Maintenance créée"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    public ResponseEntity<Maintenance> addMaintenance(@RequestBody(required = false) Maintenance maintenance) {
        if (maintenance == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.status(201).body(maintenanceService.save(maintenance));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Afficher toutes les maintenances")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des maintenances retournée")
    })
    public ResponseEntity<List<Maintenance>> getAllMaintenances() {
        return ResponseEntity.ok(maintenanceService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Afficher une maintenance par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Maintenance trouvée"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide"),
            @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    })
    public ResponseEntity<Maintenance> getMaintenanceById(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Maintenance maintenance = maintenanceService.findById(id);
        if (maintenance == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(maintenance);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une maintenance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Maintenance modifiée"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    })
    public ResponseEntity<Maintenance> updateMaintenance(@PathVariable Long id,
                                                          @RequestBody(required = false) Maintenance maintenance) {
        if (id <= 0 || maintenance == null) {
            return ResponseEntity.badRequest().build();
        }
        if (maintenanceService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        maintenance.setIdMaintenance(id);
        try {
            return ResponseEntity.ok(maintenanceService.save(maintenance));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une maintenance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Maintenance supprimée"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide ou maintenance référencée"),
            @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    })
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        if (maintenanceService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            maintenanceService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
