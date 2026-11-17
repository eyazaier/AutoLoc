package tn.esprit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.repositories.ClientRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ClientServiceImp implements IClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImp(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Client save(Client entity) {
        return clientRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }
}
