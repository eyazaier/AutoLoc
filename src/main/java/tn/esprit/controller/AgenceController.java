package tn.esprit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.services.IAgenceService;

import java.util.List;

@RestController
@RequestMapping("/api/agences")
@Tag(name = "Agences", description = "CRUD des agences de location")
public class AgenceController {

    private final IAgenceService agenceService;

    public AgenceController(IAgenceService agenceService) {
        this.agenceService = agenceService;
    }

    @PostMapping
    @Operation(summary = "Ajouter une agence")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agence créée"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    public ResponseEntity<Agence> addAgence(@RequestBody(required = false) Agence agence) {
        if (agence == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.status(201).body(agenceService.save(agence));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Afficher toutes les agences")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des agences retournée")
    })
    public ResponseEntity<List<Agence>> getAllAgences() {
        return ResponseEntity.ok(agenceService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Afficher une agence par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agence trouvée"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide"),
            @ApiResponse(responseCode = "404", description = "Agence introuvable")
    })
    public ResponseEntity<Agence> getAgenceById(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Agence agence = agenceService.findById(id);
        if (agence == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(agence);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une agence")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agence modifiée"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Agence introuvable")
    })
    public ResponseEntity<Agence> updateAgence(@PathVariable Long id,
                                                @RequestBody(required = false) Agence agence) {
        if (id <= 0 || agence == null) {
            return ResponseEntity.badRequest().build();
        }
        if (agenceService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        agence.setIdAgence(id);
        try {
            return ResponseEntity.ok(agenceService.save(agence));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une agence")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Agence supprimée"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide ou agence référencée"),
            @ApiResponse(responseCode = "404", description = "Agence introuvable")
    })
    public ResponseEntity<Void> deleteAgence(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        if (agenceService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            agenceService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
