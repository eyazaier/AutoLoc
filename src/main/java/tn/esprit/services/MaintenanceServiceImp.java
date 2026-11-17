package tn.esprit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.repositories.MaintenanceRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MaintenanceServiceImp implements IMaintenanceService {

    private final MaintenanceRepository maintenanceRepository;

    @Autowired
    public MaintenanceServiceImp(MaintenanceRepository maintenanceRepository) {
        this.maintenanceRepository = maintenanceRepository;
    }

    @Override
    public List<Maintenance> findAll() {
        return maintenanceRepository.findAll();
    }

    @Override
    public Maintenance findById(Long id) {
        return maintenanceRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Maintenance save(Maintenance entity) {
        return maintenanceRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        maintenanceRepository.deleteById(id);
    }
}
