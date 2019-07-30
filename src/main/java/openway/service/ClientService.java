package openway.service;

import openway.model.Client;
import java.util.List;

public interface ClientService {
    List<Client> findAll();
    String addNewClient(String newClient);
    String authenticationClient(String auth);
    String BalanceRequest(String data);
}