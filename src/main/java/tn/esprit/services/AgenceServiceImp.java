package tn.esprit.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repositories.AgenceRepository;
import tn.esprit.autoloc.repositories.VehiculeRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
public class AgenceServiceImp implements IAgenceService {

    private final AgenceRepository agenceRepository;
    private final VehiculeRepository vehiculeRepository;

    @Autowired
    public AgenceServiceImp(AgenceRepository agenceRepository, VehiculeRepository vehiculeRepository) {
        this.agenceRepository = agenceRepository;
        this.vehiculeRepository = vehiculeRepository;
    }

    @Override
    public List<Agence> findAll() {
        return agenceRepository.findAll();
    }

    @Override
    public Agence findById(Long id) {
        return agenceRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Agence save(Agence entity) {
        return agenceRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        agenceRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Agence ajouterAgenceEtAffecterVehicules(Agence agence) {
        if (agence == null || agence.getVehicules() == null || agence.getVehicules().isEmpty()) {
            throw new IllegalArgumentException("Une agence avec au moins un véhicule est requise");
        }
        for (Vehicule vehicule : agence.getVehicules()) {
            if (vehicule.getIdVehicule() != null) {
                throw new IllegalArgumentException("Les véhicules doivent être nouveaux (sans identifiant)");
            }
        }

        List<Vehicule> vehicules = new ArrayList<>(agence.getVehicules());
        agence.getVehicules().clear();
        Agence agenceSaved = agenceRepository.save(agence);
        for (Vehicule vehicule : vehicules) {
            Vehicule vehiculeSaved = vehiculeRepository.save(vehicule);
            agenceSaved.getVehicules().add(vehiculeSaved);
        }
        return agenceSaved;
    }

    @Scheduled(fixedDelay = 120000)
    public void suivreNombreAgences() {
        List<Agence> agences = agenceRepository.findAll();
        log.info("Vérification des agences du {} : {} agence(s) enregistrée(s)",
                LocalDateTime.now(), agences.size());
    }
}
