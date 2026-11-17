package tn.esprit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repositories.VehiculeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class VehiculeServiceImp implements IVehiculeService {

    private final VehiculeRepository vehiculeRepository;

    @Autowired
    public VehiculeServiceImp(VehiculeRepository vehiculeRepository) {
        this.vehiculeRepository = vehiculeRepository;
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
}
