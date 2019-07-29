package openway.service;


import com.google.gson.Gson;
import openway.model.Lesser;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public class UFXServiceImpl implements UFXService {

    private String IssProductCode1 = "CLIENT_ISS_001";

    private class LesserWay4{
        public String name;
        public String sName;
        public String email;
    }

    public String AddNewLessorInWay4(String newLesser){

        Gson gson = new Gson();

        gson.fromJson(newLesser, LesserWay4.class);

        String tst_sName = "tstFromServer_sName";
        String tst_Name = "tstFromServer_Name";

//        String rnd = ufxSer.GenerateId("kek");
//
//        String tst_clientNumber = rnd;
//        String tst_regNumberClient = rnd;
//        String tst_regNumberApp = rnd + "_A";
//        String tst_conractNumber = rnd;
//
//        String urlUfxAdapter = "http://10.101.124.36:17777";
//
//        String requestCreateClient = ufxSer.RequestCreateClient(tst_sName, tst_Name,
//                tst_clientNumber, tst_regNumberClient);
//        String requestCreateIssContract = ufxSer.RequestCreateIssContract(tst_clientNumber,
//                tst_regNumberClient,  tst_regNumberApp, tst_conractNumber);
//
//        logger.info("create client request: " + requestCreateClient);
//        logger.info("create client request: " + requestCreateIssContract);
//
//        String res = ufxSer.SendRequest(urlUfxAdapter, requestCreateClient);
//        logger.info(res);
//        res = ufxSer.SendRequest(urlUfxAdapter, requestCreateIssContract);
//        logger.info(res);

        return "";
    }

    /**
     *
     * @param sName surname
     * @param name name
     * @param clientNumber client id from db,
     *                     must be unique
     * @param regNumberApp application id,
     *                  must be unique
     *
     * clientNumber == regNumber in this function, but this is not the same fields in general
     *
     * @return
     */
    private String RequestCreateClient(String sName, String name,
                                      String clientNumber, String regNumberApp) {

        String res = "<UFXMsg scheme=\"WAY4Appl\" msg_type=\"Application\" version=\"2.0\" direction=\"Rq\">\n" +
                "    <MsgId>AAA-555-333-EEE-23124141</MsgId>\n" +
                "    <Source app=\"MobileApp\"/>\n" +
                "    <MsgData>\n" +
                "        <Application>\n" +
                "            <RegNumber>" +
                                regNumberApp +
                             "</RegNumber>\n" +
                "\t\t\t<Institution>0001</Institution>\n" +
                "            <OrderDprt>0101</OrderDprt>\n" +
                "            <ObjectType>Client</ObjectType>\n" +
                "            <ActionType>Add</ActionType>\n" +
                "            <Data>\n" +
                "                <Client>\n" +
                "                    <ClientType>PR</ClientType>\n" +
                "                    <ClientInfo>\n" +
                "                        <ClientNumber>" +
                                         clientNumber +
                                         "</ClientNumber>\n" +
                "                        <RegNumber>" +
                                         regNumberApp +
                                         "</RegNumber>\n" +
                "                        <RegNumberDetails>RegDetails</RegNumberDetails>\n" +
                "                        <FirstName>" +
                                         name +
                                         "</FirstName>\n" +
                "                        <LastName>" +
                                         sName +
                                         "</LastName>\n" +
                "                    </ClientInfo>\n" +
                "                </Client>\n" +
                "            </Data>\n" +
                "        </Application>\n" +
                "\t</MsgData>\n" +
                "</UFXMsg>";
        return res;
    }

    /**
     *
     * @param clientNumber client id just created in CreateClientRequest, must be the same
     * @param regNumberClient client reg number, also from createClientRequest, must be the same
     * @param regNumberApp application reg number, must be unique
     *
     * clientNumber == regNumberClient != regNumberApp
     * @param contractNumber
     * @return
     */
    private String RequestCreateIssContract(String clientNumber,
                                           String regNumberClient,
                                           String regNumberApp,
                                           String contractNumber){
        String res  = "<UFXMsg scheme=\"WAY4Appl\" msg_type=\"Application\" version=\"2.0\" direction=\"Rq\">\n" +
                "    <MsgId>AAA-555-333-EEE-23124145</MsgId>\n" +
                "    <Source app=\"MobileApp\"/>\n" +
                "    <MsgData>\n" +
                "\t\t<Application>\n" +
                "\t\t\t<RegNumber>" +
                        regNumberApp +
                        "</RegNumber>\n" +
                "\t\t\t<Institution>0001</Institution>\n" +
                "\t\t\t<OrderDprt>0101</OrderDprt>\n" +
                "\t\t\t<ObjectType>Contract</ObjectType>\n" +
                "\t\t\t<ActionType>Add</ActionType>\n" +
                "\t\t\t<ObjectFor>\n" +
                "\t\t\t\t<ClientIDT>\n" +
                "\t\t\t\t\t<ClientType>PR</ClientType>\n" +
                "\t\t\t\t\t<ClientInfo>\n" +
                "\t\t\t\t\t\t<ClientNumber>" +
                            clientNumber +
                            "</ClientNumber>\n" +
                "                        <RegNumber>" +
                                            regNumberClient +
                                         "</RegNumber>\n" +
                "\t\t\t\t\t</ClientInfo>\n" +
                "\t\t\t\t</ClientIDT>\n" +
                "\t\t\t</ObjectFor>\n" +
                "\t\t\t<Data>\n" +
                "\t\t\t\t<Contract>\n" +
                "\t\t\t\t\t<ContractIDT>\n" +
                "<ContractNumber>" + contractNumber + "</ContractNumber>" +
                "\t\t\t\t\t\t<RBSNumber>XML-BB-03-rbs_123456789123456791</RBSNumber>\n" +
                "\t\t\t\t\t</ContractIDT>\n" +
                "\t\t\t\t<Product>\n" +

                    "\t\t\t\t\t<ProductCode1>" +
                    IssProductCode1 +
                    "</ProductCode1>\n" +

                "\t\t\t\t</Product>\n" +
                "\t\t\t\t<DateOpen>2019-07-22</DateOpen>\n" +
                "\t\t\t\t<CreditLimit>\n" +
                "\t\t\t\t\t<FinanceLimit>\n" +
                "\t\t\t\t\t\t<Amount>0</Amount>\n" +
                "\t\t\t\t\t\t<Currency>USD</Currency>\n" +
                "\t\t\t\t\t</FinanceLimit>\n" +
                "\t\t\t\t<ReasonDetails>Reason details for credit limit</ReasonDetails>\n" +
                "\t\t\t\t</CreditLimit>\n" +
                "\t\t\t\t</Contract>\n" +
                "\t\t\t</Data>\n" +
                "\t\t</Application>\n" +
                "\t</MsgData>\n" +
                "</UFXMsg>";


        return res;
    }

    /** //TODO: finish doc
     *
     * @return
     */
    private String RequestCreateAcqContract(){
        return "";
    }

    /**
     *
     * @param url destination address
     * @param request request for send
     * @return
     */
    private String SendRequest(String url, String request){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response;

        try{
            StringEntity entityCreateClient = new StringEntity(request);
            httpPost.setEntity(entityCreateClient);
            httpPost.setHeader("Content-type", "text/xml");

            response = httpClient.execute(httpPost);
            System.out.println(response.toString());

            httpClient.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return e.toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return e.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }

        return response.toString();
    }


    /**
     *
     * @param data data for generation
     * @return
     */
    //TODO: finish this
    private String GenerateId(String data){
        return  "XML_SS_001";
    }
}
