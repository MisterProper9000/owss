package openway.service;

import openway.model.Client;
import openway.model.Lesser;

public interface UFXService {

    String AddNewClientInWay4(Client client);

    String AddNewLesserInWay4(Lesser lesser);

    String BalanceRequestInWay4(int clientId);

    String GetDepositFromClient(int clientId, int lesserId);

    String GetRrn();

    String reverseDeposit(int clientId, int lesserId, String RRN);

    String GetPayment(int clientId, int lesserId, float cost);

    double getDepositSize();
}

