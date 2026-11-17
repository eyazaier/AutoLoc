package tn.esprit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.repositories.ContratRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class IContratServiceImp implements IContratService {

    private final ContratRepository contratRepository;

    @Autowired
    public IContratServiceImp(ContratRepository contratRepository) {
        this.contratRepository = contratRepository;
    }

    @Override
    public List<Contrat> findAll() {
        return contratRepository.findAll();
    }

    @Override
    public Contrat findById(Long id) {
        return contratRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Contrat save(Contrat entity) {
        return contratRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        contratRepository.deleteById(id);
    }
}
