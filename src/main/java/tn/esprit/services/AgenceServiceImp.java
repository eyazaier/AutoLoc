package tn.esprit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.repositories.AgenceRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AgenceServiceImp implements IAgenceService {

    private final AgenceRepository agenceRepository;

    @Autowired
    public AgenceServiceImp(AgenceRepository agenceRepository) {
        this.agenceRepository = agenceRepository;
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
}
