package tn.esprit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.repositories.EquipementRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EquipementServiceImp implements IEquipementService {

    private final EquipementRepository equipementRepository;

    @Autowired
    public EquipementServiceImp(EquipementRepository equipementRepository) {
        this.equipementRepository = equipementRepository;
    }

    @Override
    public List<Equipement> findAll() {
        return equipementRepository.findAll();
    }

    @Override
    public Equipement findById(Long id) {
        return equipementRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Equipement save(Equipement entity) {
        return equipementRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        equipementRepository.deleteById(id);
    }
}
