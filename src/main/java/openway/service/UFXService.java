package openway.service;

import openway.model.Client;
import openway.model.Lesser;

public interface UFXService {

    String AddNewClientInWay4(Client client);

    String AddNewLesserInWay4(Lesser lesser);

    String BalanceRequestInWay4(int clientId);

    String GetDepositFromClient(int clientId, int lesserId);

    String getPayment(int clientId, int lesserId);

}

