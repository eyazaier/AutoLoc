package tn.esprit.controller;

import jakarta.persistence.EntityNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.services.IEmployeService;

import java.util.List;

@RestController
@RequestMapping("/api/employes")
@Tag(name = "Employés", description = "CRUD des employés")
public class EmployeController {

    private final IEmployeService employeService;

    public EmployeController(IEmployeService employeService) {
        this.employeService = employeService;
    }

    @PostMapping
    @Operation(summary = "Ajouter un employé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employé créé"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    public ResponseEntity<Employe> addEmploye(@RequestBody(required = false) Employe employe) {
        if (employe == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.status(201).body(employeService.save(employe));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Afficher tous les employés")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des employés retournée")
    })
    public ResponseEntity<List<Employe>> getAllEmployes() {
        return ResponseEntity.ok(employeService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Afficher un employé par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employé trouvé"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    public ResponseEntity<Employe> getEmployeById(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Employe employe = employeService.findById(id);
        if (employe == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employe);
    }

    @PutMapping("/{idEmploye}/desaffecter")
    @Operation(summary = "Désaffecter un employé de son agence")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employé désaffecté"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    public ResponseEntity<Employe> desaffecterEmploye(@PathVariable Long idEmploye) {
        if (idEmploye == null || idEmploye <= 0) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.ok(employeService.desaffecterEmploye(idEmploye));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{idEmploye}/affecter/{idAgence}")
    @Operation(summary = "Affecter un employé à une agence")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employé affecté"),
            @ApiResponse(responseCode = "400", description = "Identifiants invalides"),
            @ApiResponse(responseCode = "404", description = "Employé ou agence introuvable")
    })
    public ResponseEntity<Employe> affecterEmploye(@PathVariable Long idEmploye,
                                                    @PathVariable Long idAgence) {
        if (idEmploye == null || idEmploye <= 0 || idAgence == null || idAgence <= 0) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.ok(employeService.affecterEmploye(idEmploye, idAgence));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un employé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employé modifié"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    public ResponseEntity<Employe> updateEmploye(@PathVariable Long id,
                                                  @RequestBody(required = false) Employe employe) {
        if (id <= 0 || employe == null) {
            return ResponseEntity.badRequest().build();
        }
        if (employeService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        employe.setIdEmploye(id);
        try {
            return ResponseEntity.ok(employeService.save(employe));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un employé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employé supprimé"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide ou employé référencé"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    public ResponseEntity<Void> deleteEmploye(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        if (employeService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            employeService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
