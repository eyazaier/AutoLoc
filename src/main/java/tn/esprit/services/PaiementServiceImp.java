package tn.esprit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.repositories.PaiementRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PaiementServiceImp implements IPaiementService {

    private final PaiementRepository paiementRepository;

    @Autowired
    public PaiementServiceImp(PaiementRepository paiementRepository) {
        this.paiementRepository = paiementRepository;
    }

    @Override
    public List<Paiement> findAll() {
        return paiementRepository.findAll();
    }

    @Override
    public Paiement findById(Long id) {
        return paiementRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Paiement save(Paiement entity) {
        return paiementRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        paiementRepository.deleteById(id);
    }
}
