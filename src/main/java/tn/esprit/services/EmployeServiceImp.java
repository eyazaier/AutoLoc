package tn.esprit.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.repositories.AgenceRepository;
import tn.esprit.autoloc.repositories.EmployeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EmployeServiceImp implements IEmployeService {

    private final EmployeRepository employeRepository;
    private final AgenceRepository agenceRepository;

    @Autowired
    public EmployeServiceImp(EmployeRepository employeRepository, AgenceRepository agenceRepository) {
        this.employeRepository = employeRepository;
        this.agenceRepository = agenceRepository;
    }

    @Override
    public List<Employe> findAll() {
        return employeRepository.findAll();
    }

    @Override
    public Employe findById(Long id) {
        return employeRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Employe save(Employe entity) {
        return employeRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        employeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Employe desaffecterEmploye(Long idEmploye) {
        Employe employe = employeRepository.findById(idEmploye)
                .orElseThrow(() -> new EntityNotFoundException("Employé introuvable : " + idEmploye));
        employe.setAgence(null);
        return employeRepository.save(employe);
    }

    @Override
    @Transactional
    public Employe affecterEmploye(Long idEmploye, Long idAgence) {
        Employe employe = employeRepository.findById(idEmploye)
                .orElseThrow(() -> new EntityNotFoundException("Employé introuvable : " + idEmploye));
        Agence agence = agenceRepository.findById(idAgence)
                .orElseThrow(() -> new EntityNotFoundException("Agence introuvable : " + idAgence));
        employe.setAgence(agence);
        return employeRepository.save(employe);
    }
}
