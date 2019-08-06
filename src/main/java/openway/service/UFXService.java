package openway.service;

import openway.model.Client;
import openway.model.Lesser;

public interface UFXService {

    String AddNewClientInWay4(Client client);

    String AddNewLesserInWay4(Lesser lesser);

    String BalanceRequestInWay4(int clientId);

    String GetDepositFromClient(int clientId, int lesserId);

    String ReverseDeposit(int clientId, int lesserId, String RRN);

    String GetRrn();

    String GetPayment(int clientId, int lesserId, float cost);

    double getDepositSize();

    String BalanceLesserRequestInWay4(int clientId);

    String GenerateRRN(String type);

    String CheckRes(String res);

    String ClientTopUp(String name, String sName, String cardNum,
                              String cvc2, String exDate,String amount, int clientId);

    String LesserTopUp(String name, String sName, String cardNum,
                       String cvc2, String exDate,String amount, int clientId);

}

