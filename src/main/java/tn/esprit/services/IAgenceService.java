package tn.esprit.services;

import tn.esprit.autoloc.domain.Agence;

import java.util.List;

public interface IAgenceService {

    List<Agence> findAll();

    Agence findById(Long id);

    Agence save(Agence entity);

    void deleteById(Long id);

    Agence ajouterAgenceEtAffecterVehicules(Agence agence);
}
