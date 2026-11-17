package tn.esprit.services;

import tn.esprit.autoloc.domain.Paiement;

import java.util.List;

public interface IPaiementService {

    List<Paiement> findAll();

    Paiement findById(Long id);

    Paiement save(Paiement entity);

    void deleteById(Long id);
}
