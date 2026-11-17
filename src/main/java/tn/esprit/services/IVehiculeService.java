package tn.esprit.services;

import tn.esprit.autoloc.domain.Vehicule;

import java.util.List;

public interface IVehiculeService {

    List<Vehicule> findAll();

    Vehicule findById(Long id);

    Vehicule save(Vehicule entity);

    void deleteById(Long id);
}
