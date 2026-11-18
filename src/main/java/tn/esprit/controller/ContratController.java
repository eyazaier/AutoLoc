package tn.esprit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.services.IContratService;

import java.util.List;

@RestController
@RequestMapping("/api/contrats")
@Tag(name = "Contrats", description = "CRUD des contrats de location")
public class ContratController {

    private final IContratService contratService;

    public ContratController(IContratService contratService) {
        this.contratService = contratService;
    }

    @PostMapping
    @Operation(summary = "Ajouter un contrat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contrat créé"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    public ResponseEntity<Contrat> addContrat(@RequestBody(required = false) Contrat contrat) {
        if (contrat == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.status(201).body(contratService.save(contrat));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Afficher tous les contrats")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des contrats retournée")
    })
    public ResponseEntity<List<Contrat>> getAllContrats() {
        return ResponseEntity.ok(contratService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Afficher un contrat par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contrat trouvé"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    public ResponseEntity<Contrat> getContratById(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Contrat contrat = contratService.findById(id);
        if (contrat == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contrat);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un contrat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contrat modifié"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    public ResponseEntity<Contrat> updateContrat(@PathVariable Long id,
                                                  @RequestBody(required = false) Contrat contrat) {
        if (id <= 0 || contrat == null) {
            return ResponseEntity.badRequest().build();
        }
        if (contratService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        contrat.setIdContrat(id);
        try {
            return ResponseEntity.ok(contratService.save(contrat));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un contrat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contrat supprimé"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide ou contrat référencé"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    public ResponseEntity<Void> deleteContrat(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        if (contratService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            contratService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
