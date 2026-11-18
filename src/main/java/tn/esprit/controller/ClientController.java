package tn.esprit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.services.IClientService;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "CRUD des clients")
public class ClientController {

    private final IClientService clientService;

    public ClientController(IClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @Operation(summary = "Ajouter un client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client créé"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    public ResponseEntity<Client> addClient(@RequestBody(required = false) Client client) {
        if (client == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.status(201).body(clientService.save(client));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Afficher tous les clients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des clients retournée")
    })
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(clientService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Afficher un client par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client trouvé"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide"),
            @ApiResponse(responseCode = "404", description = "Client introuvable")
    })
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Client client = clientService.findById(id);
        if (client == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(client);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client modifié"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Client introuvable")
    })
    public ResponseEntity<Client> updateClient(@PathVariable Long id,
                                                @RequestBody(required = false) Client client) {
        if (id <= 0 || client == null) {
            return ResponseEntity.badRequest().build();
        }
        if (clientService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        client.setIdClient(id);
        try {
            return ResponseEntity.ok(clientService.save(client));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client supprimé"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide ou client référencé"),
            @ApiResponse(responseCode = "404", description = "Client introuvable")
    })
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        if (clientService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            clientService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
