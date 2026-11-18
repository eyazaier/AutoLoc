package tn.esprit.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repositories.EquipementRepository;
import tn.esprit.autoloc.repositories.MaintenanceRepository;
import tn.esprit.autoloc.repositories.ReservationRepository;
import tn.esprit.autoloc.repositories.VehiculeRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@Slf4j
public class VehiculeServiceImp implements IVehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final EquipementRepository equipementRepository;
    private final ReservationRepository reservationRepository;
    private final MaintenanceRepository maintenanceRepository;

    @Autowired
    public VehiculeServiceImp(VehiculeRepository vehiculeRepository, EquipementRepository equipementRepository,
                              ReservationRepository reservationRepository, MaintenanceRepository maintenanceRepository) {
        this.vehiculeRepository = vehiculeRepository;
        this.equipementRepository = equipementRepository;
        this.reservationRepository = reservationRepository;
        this.maintenanceRepository = maintenanceRepository;
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

    @Scheduled(fixedRate = 60000, initialDelay = 5000)
    @Transactional
    public void actualiserEtatVehicules() {
        LocalDate aujourdHui = LocalDate.now();
        List<Vehicule> vehicules = vehiculeRepository.findAll();
        List<Reservation> reservations = reservationRepository.findAll();
        List<Maintenance> maintenances = maintenanceRepository.findAll();
        log.info("Actualisation des véhicules du {} : {} véhicule(s) enregistré(s)",
                LocalDateTime.now(), vehicules.size());
        List<Vehicule> vehiculesModifies = new ArrayList<>();

        for (Vehicule vehicule : vehicules) {
            Long idVehicule = vehicule.getIdVehicule();
            StatutVehicule statutInitial = vehicule.getStatut();

            boolean maintenanceEnCours = maintenances.stream()
                    .anyMatch(m -> Objects.equals(idVehicule, getIdVehicule(m.getVehicule()))
                            && m.getDateDebut() != null && !m.getDateDebut().isAfter(aujourdHui)
                            && (m.getDateFin() == null || !m.getDateFin().isBefore(aujourdHui)));
            if (maintenanceEnCours) {
                if (statutInitial != StatutVehicule.MAINTENANCE) {
                    vehicule.setStatut(StatutVehicule.MAINTENANCE);
                    vehiculesModifies.add(vehicule);
                    log.info("Véhicule {} ({}) : {} -> MAINTENANCE", idVehicule,
                            vehicule.getImmatriculation(), statutInitial);
                }
                continue;
            }

            boolean locationEnCours = reservations.stream()
                    .anyMatch(r -> Objects.equals(idVehicule, getIdVehicule(r.getVehicule()))
                            && r.getStatut() == StatutReservation.CONFIRMEE
                            && r.getDateDebut() != null && !r.getDateDebut().isAfter(aujourdHui)
                            && r.getDateFin() != null && !r.getDateFin().isBefore(aujourdHui));
            if (locationEnCours) {
                if (statutInitial != StatutVehicule.LOUE) {
                    vehicule.setStatut(StatutVehicule.LOUE);
                    vehiculesModifies.add(vehicule);
                    log.info("Véhicule {} ({}) : {} -> LOUE", idVehicule,
                            vehicule.getImmatriculation(), statutInitial);
                }
                continue;
            }

            boolean aDesDatesExploitables = reservations.stream()
                    .anyMatch(r -> Objects.equals(idVehicule, getIdVehicule(r.getVehicule()))
                            && r.getDateFin() != null)
                    || maintenances.stream()
                    .anyMatch(m -> Objects.equals(idVehicule, getIdVehicule(m.getVehicule()))
                            && m.getDateFin() != null);

            if (aDesDatesExploitables && (statutInitial == StatutVehicule.LOUE
                    || statutInitial == StatutVehicule.MAINTENANCE)) {
                vehicule.setStatut(StatutVehicule.DISPONIBLE);
                vehiculesModifies.add(vehicule);
                log.info("Véhicule {} ({}) : {} -> DISPONIBLE", idVehicule,
                        vehicule.getImmatriculation(), statutInitial);
            }
        }

        if (!vehiculesModifies.isEmpty()) {
            vehiculeRepository.saveAll(vehiculesModifies);
        }
        log.info("Scheduler état véhicules : {} véhicule(s) vérifié(s), {} modification(s)",
                vehicules.size(), vehiculesModifies.size());
    }

    @Scheduled(cron = "0 0 9 * * MON")
    public void genererRapportVehicules() {
        List<Vehicule> vehicules = vehiculeRepository.findAll();
        log.info("Rapport véhicules généré le {} : total = {} véhicule(s)",
                LocalDateTime.now(), vehicules.size());
    }

    private Long getIdVehicule(Vehicule vehicule) {
        return vehicule == null ? null : vehicule.getIdVehicule();
    }
}
