package openway.service;

import openway.model.Client;
import openway.model.Lesser;

public interface UFXService {

    String AddNewClientInWay4(Client client);

    String AddNewLesserInWay4(Lesser lesser);

    String BalanceRequestInWay4(String clientNumber);

    String GetDepositFromClient(int clientId, int lesserId);

}

