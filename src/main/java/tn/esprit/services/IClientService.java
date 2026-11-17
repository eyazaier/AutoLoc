package tn.esprit.services;

import tn.esprit.autoloc.domain.Client;

import java.util.List;

public interface IClientService {

    List<Client> findAll();

    Client findById(Long id);

    Client save(Client entity);

    void deleteById(Long id);
}
