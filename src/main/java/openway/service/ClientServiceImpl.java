package openway.service;

import com.google.gson.Gson;
import openway.model.Client;
import openway.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class ClientServiceImpl implements ClientService {
    private final static Logger logger = Logger.getLogger(ClientServiceImpl.class.getName());
    final private ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public void addNewClient(String newClient) {
        logger.info("called addNewLesser()");
        Gson g = new Gson();
        Client client = g.fromJson(newClient,Client.class);
        clientRepository.save(client);
        logger.info("save to database:" + client);
    }
}
