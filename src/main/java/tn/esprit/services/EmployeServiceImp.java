package tn.esprit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.repositories.EmployeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EmployeServiceImp implements IEmployeService {

    private final EmployeRepository employeRepository;

    @Autowired
    public EmployeServiceImp(EmployeRepository employeRepository) {
        this.employeRepository = employeRepository;
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
}
