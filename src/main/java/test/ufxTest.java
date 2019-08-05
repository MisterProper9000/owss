package test;

import openway.model.Client;
import openway.model.Lesser;
import openway.model.Motoroller;
import openway.repository.ClientRepository;
import openway.service.MotoService;
import openway.service.MotoServiceImpl;
import openway.service.UFXService;
import openway.service.UFXServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ufxTest {

    public static void main(String argv[]){

        UFXService ufxSer = new UFXServiceImpl();
        //String req = ufxSer.ClientTopUp("Daniil", "Plaksin",
        //        "1000040176379547", "970", "2008", "25",
        //        1);


//        Motoroller moto = motoRepository.findMotorollerById(id);
//        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
//        Date currentDate = new Date();
//        Date dateStart = new Date();
//        try {
//            dateStart = formatter.parse("05-08-2019 15:15:34");
//
//        }catch (Exception e)
//        {
//            System.out.println(e.toString());
//        }
//
//        System.out.println((currentDate.getTime() - dateStart.getTime()) / ( 60 * 1000));
        ; //formatter.parse(moto.getTime_res_st())

        //System.out.println(req);
//
//        String kek = "sfb_moto:1|danyaplaksin@gmail.com";
//        String rmail = kek.split("\\|")[1];
//        System.out.println(rmail);

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

       // ufxSer.GetPayment(1,1,20);
        //String bal = ufxSer.BalanceRequestInWay4(1);
        //System.out.println(bal);



    }

}
