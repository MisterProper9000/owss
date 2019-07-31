package openway.service;

import com.google.gson.Gson;
import openway.model.Client;
import openway.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    public String addNewClient(String newClient) {
        logger.info("called addNewLesser()");
        Gson g = new Gson();
        UFXService ufxService = new UFXServiceImpl();

        Client clientDb = g.fromJson(newClient, Client.class);

        try {
            clientRepository.save(clientDb);
            logger.info("saved to database: " + clientDb);

            String res = ufxService.AddNewClientInWay4(clientDb);
            logger.info("saved to way4: " + res);

            return String.valueOf(Status.OK);
        } catch (DataIntegrityViolationException e) {
            return String.valueOf(Status.ALREADYEXIST);
        } catch (Exception e) {
            return String.valueOf(Status.OTHER);
        }
    }

    @Override
    public String authenticationClient(String auth) {
        logger.info("called authentication()" + auth);
        Gson g = new Gson();
        Client client = g.fromJson(auth, Client.class);
        logger.info("err" + client);


//        logger.info(String.valueOf(clientRepository.findClientByEmail(client.getEmail())));
//        logger.info("test client" + client.getEmail() + client.getPassword());
//        logger.info("test clientInDB" + clientInDB.getEmail() + clientInDB.getPassword());
        try {
            Client clientInDB = clientRepository.findClientByEmail(client.getEmail());
            if (clientInDB.getPassword().equals(client.getPassword())) {
                logger.info("checked entered data from start page");
                return (Status.OK + "|" + clientInDB.getFirst_name() + "|" + clientInDB.getLast_name());
            }
        } catch (NullPointerException e) {
            logger.info("login or password incorrect");
            return (String.valueOf(Status.DOESNTEXIST));
        }
        return String.valueOf(Status.OTHER);
    }

    @Override
    public boolean isEmailOfClientExist(String email) {
        return clientRepository.findClientByEmail(email).getEmail() != null;
    }

    @Override
    public String CheckBalance(String data){
        logger.info("called check balance" + data);
        //Gson g = new Gson();
        //Client client = g.fromJson(data, Client.class);
        String clientNumber = Integer.toString(clientRepository.findClientByEmail(data).getId());
        UFXService ufxService = new UFXServiceImpl();

        String balance = ufxService.BalanceRequestInWay4(clientNumber);

        return balance;
    }
}