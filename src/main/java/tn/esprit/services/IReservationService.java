package tn.esprit.services;

import tn.esprit.autoloc.domain.Reservation;

import java.util.List;

public interface IReservationService {

    List<Reservation> findAll();

    Reservation findById(Long id);

    Reservation save(Reservation entity);

    void deleteById(Long id);
}
