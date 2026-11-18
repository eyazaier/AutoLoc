package tn.esprit.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repositories.EquipementRepository;
import tn.esprit.autoloc.repositories.VehiculeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class VehiculeServiceImp implements IVehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final EquipementRepository equipementRepository;

    @Autowired
    public VehiculeServiceImp(VehiculeRepository vehiculeRepository, EquipementRepository equipementRepository) {
        this.vehiculeRepository = vehiculeRepository;
        this.equipementRepository = equipementRepository;
    }

    @Override
    public List<Vehicule> findAll() {
        return vehiculeRepository.findAll();
    }

    @Override
    public Vehicule findById(Long id) {
        return vehiculeRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Vehicule save(Vehicule entity) {
        return vehiculeRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        vehiculeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Vehicule affecterEquipement(Long idVehicule, Long idEquipement) {
        Vehicule vehicule = vehiculeRepository.findById(idVehicule)
                .orElseThrow(() -> new EntityNotFoundException("Véhicule introuvable : " + idVehicule));
        Equipement equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new EntityNotFoundException("Équipement introuvable : " + idEquipement));

        boolean dejaAffecte = vehicule.getEquipements().stream()
                .anyMatch(e -> e.getIdEquipement().equals(idEquipement));
        if (!dejaAffecte) {
            vehicule.getEquipements().add(equipement);
        }
        return vehiculeRepository.save(vehicule);
    }

    @Override
    @Transactional
    public Vehicule desaffecterEquipement(Long idVehicule, Long idEquipement) {
        Vehicule vehicule = vehiculeRepository.findById(idVehicule)
                .orElseThrow(() -> new EntityNotFoundException("Véhicule introuvable : " + idVehicule));
        Equipement equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new EntityNotFoundException("Équipement introuvable : " + idEquipement));

        vehicule.getEquipements().removeIf(e -> e.getIdEquipement().equals(equipement.getIdEquipement()));
        return vehiculeRepository.save(vehicule);
    }
}
