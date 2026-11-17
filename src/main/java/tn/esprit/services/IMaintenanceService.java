package tn.esprit.services;

import tn.esprit.autoloc.domain.Maintenance;

import java.util.List;

public interface IMaintenanceService {

    List<Maintenance> findAll();

    Maintenance findById(Long id);

    Maintenance save(Maintenance entity);

    void deleteById(Long id);
}
