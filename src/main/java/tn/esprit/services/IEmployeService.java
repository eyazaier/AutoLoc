package tn.esprit.services;

import tn.esprit.autoloc.domain.Employe;

import java.util.List;

public interface IEmployeService {

    List<Employe> findAll();

    Employe findById(Long id);

    Employe save(Employe entity);

    void deleteById(Long id);
}
