package openway.service;

import openway.model.Client;
import openway.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
   final private ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository motoRepository) {
        this.clientRepository = motoRepository;
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }
}
