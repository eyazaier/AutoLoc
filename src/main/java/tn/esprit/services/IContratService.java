package tn.esprit.services;

import tn.esprit.autoloc.domain.Contrat;

import java.util.List;

public interface IContratService {

    List<Contrat> findAll();

    Contrat findById(Long id);

    Contrat save(Contrat entity);

    void deleteById(Long id);
}
