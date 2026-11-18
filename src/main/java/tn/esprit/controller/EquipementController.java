package tn.esprit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.services.IEquipementService;

import java.util.List;

@RestController
@RequestMapping("/api/equipements")
@Tag(name = "Équipements", description = "CRUD des équipements disponibles")
public class EquipementController {

    private final IEquipementService equipementService;

    public EquipementController(IEquipementService equipementService) {
        this.equipementService = equipementService;
    }

    @PostMapping
    @Operation(summary = "Ajouter un équipement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Équipement créé"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    public ResponseEntity<Equipement> addEquipement(@RequestBody(required = false) Equipement equipement) {
        if (equipement == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.status(201).body(equipementService.save(equipement));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Afficher tous les équipements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des équipements retournée")
    })
    public ResponseEntity<List<Equipement>> getAllEquipements() {
        return ResponseEntity.ok(equipementService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Afficher un équipement par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Équipement trouvé"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    })
    public ResponseEntity<Equipement> getEquipementById(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Equipement equipement = equipementService.findById(id);
        if (equipement == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(equipement);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un équipement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Équipement modifié"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    })
    public ResponseEntity<Equipement> updateEquipement(@PathVariable Long id,
                                                        @RequestBody(required = false) Equipement equipement) {
        if (id <= 0 || equipement == null) {
            return ResponseEntity.badRequest().build();
        }
        if (equipementService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        equipement.setIdEquipement(id);
        try {
            return ResponseEntity.ok(equipementService.save(equipement));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un équipement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Équipement supprimé"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide ou équipement référencé"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    })
    public ResponseEntity<Void> deleteEquipement(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        if (equipementService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            equipementService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
