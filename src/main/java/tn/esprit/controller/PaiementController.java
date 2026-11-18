package tn.esprit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.services.IPaiementService;

import java.util.List;

@RestController
@RequestMapping("/api/paiements")
@Tag(name = "Paiements", description = "CRUD des paiements des contrats")
public class PaiementController {

    private final IPaiementService paiementService;

    public PaiementController(IPaiementService paiementService) {
        this.paiementService = paiementService;
    }

    @PostMapping
    @Operation(summary = "Ajouter un paiement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paiement créé"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    public ResponseEntity<Paiement> addPaiement(@RequestBody(required = false) Paiement paiement) {
        if (paiement == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.status(201).body(paiementService.save(paiement));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Afficher tous les paiements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des paiements retournée")
    })
    public ResponseEntity<List<Paiement>> getAllPaiements() {
        return ResponseEntity.ok(paiementService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Afficher un paiement par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paiement trouvé"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide"),
            @ApiResponse(responseCode = "404", description = "Paiement introuvable")
    })
    public ResponseEntity<Paiement> getPaiementById(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Paiement paiement = paiementService.findById(id);
        if (paiement == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(paiement);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un paiement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paiement modifié"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Paiement introuvable")
    })
    public ResponseEntity<Paiement> updatePaiement(@PathVariable Long id,
                                                    @RequestBody(required = false) Paiement paiement) {
        if (id <= 0 || paiement == null) {
            return ResponseEntity.badRequest().build();
        }
        if (paiementService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        paiement.setIdPaiement(id);
        try {
            return ResponseEntity.ok(paiementService.save(paiement));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un paiement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paiement supprimé"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide ou paiement référencé"),
            @ApiResponse(responseCode = "404", description = "Paiement introuvable")
    })
    public ResponseEntity<Void> deletePaiement(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        if (paiementService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            paiementService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
