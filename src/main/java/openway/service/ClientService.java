package openway.service;

import openway.model.Client;
import java.util.List;

public interface ClientService {
    List<Client> findAll();
    String addNewClient(String newClient);
    String authenticationClient(String auth);
    boolean isEmailOfClientExist(String email);
    String CheckBalance(String data);
    String payRent(String data);
}