package openway.service;

import com.google.gson.Gson;
import openway.model.Client;
import openway.model.Motoroller;
import openway.model.Order;
import openway.repository.ClientRepository;
import openway.repository.MotoRepository;
import openway.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class ClientServiceImpl implements ClientService {

    private final static Logger logger = Logger.getLogger(ClientServiceImpl.class.getName());
    final private ClientRepository clientRepository;
    final private OrderRepository orderRepository;
    final private MotoRepository motoRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository,
                             OrderRepository orderRepository,
                             MotoRepository motoRepository) {
        this.clientRepository = clientRepository;
        this.orderRepository = orderRepository;
        this.motoRepository = motoRepository;
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
            String checkResWay4 = ufxService.CheckRes(res);
            logger.info("saved to way4: " + checkResWay4);
            if(!checkResWay4.equals(String.valueOf(Status.OK))){
                return String.valueOf(Status.ERROR);
            }

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
        int clientId = 0;
        try{
            clientId = clientRepository.findClientByEmail(data).getId();
        } catch (Exception e)
        {
            logger.info("database error: " + e.toString());
        }
        logger.info("database request client by ID " + clientId + "succesfully finded");


        UFXService ufxService = new UFXServiceImpl();
        String balance = ufxService.BalanceRequestInWay4(clientId);

        return balance;
    }

    @Override
    public String payRent(String data){
        logger.info("called payRent " + data);
        UFXService ufxService = new UFXServiceImpl();
        int orderId = Integer.valueOf(data);
        Order order = orderRepository.findOrderById(orderId);
        int clientId = order.getIdclient();
        float cost = order.getCost();
        int motoId = order.getId_moto();
        Motoroller moto = motoRepository.findMotorollerById(motoId);
        int lesserId = moto.getIdowner();

        String status = ufxService.GetPayment(clientId, lesserId, cost);
        logger.info(status);
        return String.valueOf(Status.OK);
    }

    @Override
    public String TopUp(String topUpData){
        String args[] = topUpData.split("\\|");
        String name = "";
        String sName = "";
        String cardNum = args[0];
        String cvc2 = args[1];
        String exDate = args[2];
        String amount = args[3];
        String email = args[4];
        UFXService ufxService = new UFXServiceImpl();
        Client client = clientRepository.findClientByEmail(email);
        int clientId = client.getId();

        String resTopUp = ufxService.ClientTopUp(name, sName, cardNum, cvc2,
                exDate, amount, clientId);

        return resTopUp;
    }

}