package tn.esprit.services;

import tn.esprit.autoloc.domain.Equipement;

import java.util.List;

public interface IEquipementService {

    List<Equipement> findAll();

    Equipement findById(Long id);

    Equipement save(Equipement entity);

    void deleteById(Long id);
}
