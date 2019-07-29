package test;

import openway.service.UFXService;
import openway.service.UFXServiceImpl;

import java.io.FileWriter;
import java.io.IOException;

public class ufxTest {

    public static void main(String argv[]){
        UFXService ufxSer = new UFXServiceImpl();

        String tst_sName = "tst_sName_08";
        String tst_Name = "tst_Name_08";
        String def = ufxSer.GenerateId("kek");
        String tst_clientNumber = def;
        String tst_regNumberClient = def;


        String requestCreateClient = ufxSer.RequestCreateClient(tst_sName, tst_Name,
                tst_clientNumber, tst_regNumberClient);


        String tst_regNumberApp = def + "_A";
        String tst_contractNumber = def;
        String requestCreateIssContract = ufxSer.RequestCreateIssContract(tst_clientNumber,
                tst_regNumberClient,  tst_regNumberApp, tst_contractNumber);

        //System.out.println(requestCreateClient);
        //System.out.println(requestCreateIssContract);

        String urlUfxAdapter = "http://10.101.124.36:17777";

        String responseClient = ufxSer.SendRequest(urlUfxAdapter, requestCreateClient).toString();
        String responseContract = ufxSer.SendRequest(urlUfxAdapter, requestCreateIssContract).toString();

        try {
            FileWriter writer = new FileWriter("log.txt");
            writer.write(responseClient);
            writer.write(responseContract);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
