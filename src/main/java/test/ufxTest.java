package test;

import openway.model.Client;
import openway.model.Lesser;
import openway.repository.ClientRepository;
import openway.service.UFXService;
import openway.service.UFXServiceImpl;


public class ufxTest {

    public static void main(String argv[]){

        UFXService ufxSer = new UFXServiceImpl();

        String req = ufxSer.ClientTopUp("Daniil", "Plaksin",
                "1000040176379547", "970", "2008", "25",
                1);

        System.out.println(req);

//        Client client = new Client("Vasya", "Lalkov", "vl@gmail.com",
//                "5553535", "123");

        //client.setId(34);

        //ufxSer.AddNewClientInWay4(client);

//        ufxSer.GetDepositFromClient(1,1);
//        String rrn = ufxSer.GetRrn();
//
//        String bal = ufxSer.BalanceRequestInWay4(1);
//        System.out.println(bal);
//
//        String res = ufxSer.reverseDeposit(1,1, rrn);
//        System.out.println(res);
//        //System.out.println(ufxSer.GetDepositFromClient(10, 0));
//
//        bal = ufxSer.BalanceRequestInWay4(1);
//        System.out.println(bal);
        //ufxSer.GetPayment(1,1,20);
        //String bal = ufxSer.BalanceRequestInWay4(1);
        //System.out.println(bal);



    }

}
