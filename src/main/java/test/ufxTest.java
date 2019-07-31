package test;

import openway.model.Client;
import openway.model.Lesser;
import openway.service.UFXService;
import openway.service.UFXServiceImpl;


public class ufxTest {

    public static void main(String argv[]){

        UFXService ufxSer = new UFXServiceImpl();
//        Client client = new Client("Vasya", "Lalkov", "vl@gmail.com",
//                "5553535", "123");

        //client.setId(34);

        //ufxSer.AddNewClientInWay4(client);


        String bal = ufxSer.BalanceRequestInWay4(10);
        System.out.println(bal);

        //System.out.println(ufxSer.GetDepositFromClient(10, 0));


    }

}
