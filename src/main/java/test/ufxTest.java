package test;

import openway.model.Client;
import openway.model.Lesser;
import openway.repository.ClientRepository;
import openway.service.UFXService;
import openway.service.UFXServiceImpl;


public class ufxTest {

    public static void main(String argv[]){

        UFXService ufxSer = new UFXServiceImpl();
        System.out.println(ufxSer.LesserUpdMotoInfo(1, 2, "1000040182277768"));


        //Client client = new Client("Vasya", "Lalkov", "vl@gmail.com",
        //"5553535", "123");
        //String newClientRes = ufxSer.AddNewClientInWay4(client);

//        Lesser lesser = new Lesser("TestType", "Vasiliy",
//                "Ivanovich", "AcqEmail@mail.com",
//                "pass", "5553535", "kekova47", "1000040182277768",
//                5);

        //String newLesserRes = ufxSer.AddNewLesserInWay4(lesser);


//        String req = ufxSer.LesserTopUp("Daniil", "Plaksin",
//                "1000040176379547", "970", "2008", "25",
//                0);

        //System.out.println(req);
//        System.out.println(req);

//        String kek = "sfb_moto:1|danyaplaksin@gmail.com";
//        String rmail = kek.split("\\|")[1];
//        System.out.println(rmail);



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

       // ufxSer.GetPayment(1,1,20);
        //String bal = ufxSer.BalanceRequestInWay4(1);
        //System.out.println(bal);



    }

}
