package openway.service;

import openway.model.Client;
import openway.model.Lesser;
import openway.utils.XMLParse;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UFXServiceImpl implements UFXService {

    private final static Logger logger = Logger.getLogger(UFXServiceImpl.class.getName());
    private String IssProductCode1 = "CLIENT_ISS_001";
    private String AcqProductCode1 = "LESSER_ASQ_001";
    private String urlUfxAdapter = "http://10.101.124.36:17777";

    /**
     *
     * @param client class client with data for creating
     * @return string with result of 2 request^ create client iss + create contract iss
     */
    public String AddNewClientInWay4(Client client){

        String name = client.getFirst_name();
        String sName = client.getLast_name();

        String rnd = GenerateId("") + client.getId();

        String clientNumber = rnd;
        String regNumberClient = rnd;
        String regNumberApp = rnd + "_A";
        String contractNumber = rnd;

        String requestCreateClient = RequestCreateClient(sName, name,
                clientNumber, regNumberClient);
        String requestCreateIssContract = RequestCreateIssContract(clientNumber,
                regNumberClient,  regNumberApp, contractNumber);

        logger.info("create client request: " + requestCreateClient);
        logger.info("create client request: " + requestCreateIssContract);

        String resCl = SendRequest(urlUfxAdapter, requestCreateClient);
        logger.info(resCl);
        String resCt = SendRequest(urlUfxAdapter, requestCreateIssContract);
        logger.info(resCt);

        return resCl + "\n " + resCt;
    }

    public String AddNewLesserInWay4(Lesser lesser){
        String email = lesser.getEmail();
        String name = lesser.getFirst_name();
        String sName = lesser.getLast_name();
        String companyName = lesser.getCompany_name();

        String rnd = GenerateId("") + lesser.getId();
        String clientNumber = rnd;
        String regNumberClient = rnd;
        String regNumberApp = rnd + "_A";
        String contractNumber = rnd;
        String contractName = contractNumber + "_N";

        String requestAcqContract = RequestCreateAcqContract(sName, name, companyName,
                email, clientNumber, regNumberClient, regNumberApp, contractNumber, contractName);

        String res = SendRequest(urlUfxAdapter, requestAcqContract);

        return res;
    }

    public String BalanceRequestInWay4(String clientNumber){
        String tmp = GenerateId("") + clientNumber;
        String request = RequestCreateBalanceInquery(tmp);
        String response = SendRequest(urlUfxAdapter, request);
        String balance = BalanceResponseParse(response);
        return balance;
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(new Date());

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
                "\t\t\t\t<DateOpen>" +
                dateString +
                "</DateOpen>\n" +
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
    private String RequestCreateAcqContract(String sName, String name,
                                            String companyName, String email,
                                            String clientNumber,
                                            String regNumberClient,
                                            String regNumberApp,
                                            String contractNumber,
                                            String contractName){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(new Date());

        String res = "<UFXMsg scheme=\"WAY4Appl\" msg_type=\"Application\" version=\"2.0\" direction=\"Rq\">\n" +
                "    <MsgId>AAA-555-333-EEE-23124141</MsgId>\n" +
                "    <Source app=\"WebApp\"/>\n" +
                "    <MsgData>\n" +
                "        <Application>\n" +
                "            <RegNumber>" +
                             regNumberApp +
                             "</RegNumber>\n" +
                "            <Institution>0001</Institution>\n" +
                "            <OrderDprt>0101</OrderDprt>\n" +
                "            <ObjectType>ClientContract</ObjectType>\n" +
                "            <ActionType>Add</ActionType>\n" +
                "            <Data>\n" +
                "                <ClientContract>\n" +
                "                    <Client>\n" +
                "                        <ClientType>CR</ClientType>\n" +
                "                        <ClientInfo>\n" +
                "                            <ClientNumber>" +
                                            clientNumber +
                                            "</ClientNumber>\n" +
                "                            <RegNumber>" +
                                            regNumberClient +
                                            "</RegNumber>\n" +
                "                            <FirstName>" +
                                            name +
                                            "</FirstName>\n" +
                "                            <LastName>" +
                                            sName +
                                            "</LastName>\n" +
                "                            <CompanyName>" +
                                            companyName +
                                            "</CompanyName>\n" +
                "                        </ClientInfo>\n" +
                "                        <BaseAddress>\n" +
                "                            <EMail>" +
                                            email +
                                            "</EMail>\n" +
                "                        </BaseAddress>\n" +
                "                    </Client>\n" +
                "                    <Contract>\n" +
                "                        <ContractIDT>\n" +
                "                            <!-- contractNumber == clientNumber-->\n" +
                "                            <ContractNumber>" +
                                            contractNumber +
                                            "</ContractNumber>\n" +
                "                        </ContractIDT>\n" +
                "                        <ContractName>" +
                                        contractName +
                                        "</ContractName>\n" +
                "                        <Product>\n" +
                "                            <ProductCode1>" +
                                            AcqProductCode1 +
                                            "</ProductCode1>\n" +
                "                        </Product>\n" +
                "                        <DateOpen>" +
                                        dateString +
                                        "</DateOpen>\n" +
                "                    </Contract>\n" +
                "                </ClientContract>\n" +
                "            </Data>\n" +
                "        </Application>\n" +
                "    </MsgData>\n" +
                "</UFXMsg>";

        return res;
    }


    private String RequestCreateBalanceInquery(String clientNumber){
        String regNumberApp = clientNumber + "_B";
        String request = "<UFXMsg direction=\"Rq\" msg_type=\"Information\" scheme=\"WAY4Appl\" version=\"2.0\">\n" +
                "    <MsgId>AAA-555-333-EEE-23124141</MsgId>\n" +
                "    <Source app=\"MobileApp\"/>\n" +
                "    <MsgData>\n" +
                "        <Information>\n" +
                "            <RegNumber>" +
                            regNumberApp +
                            "</RegNumber>\n" +
                "            <ObjectType>Contract</ObjectType>\n" +
                "            <ActionType>Inquiry</ActionType>\n" +
                "            <ResultDtls>\n" +
                "                <Parm>\n" +
                "                    <ParmCode>Status</ParmCode>\n" +
                "                    <Value>Y</Value>\n" +
                "                </Parm>\n" +
                "                <Parm>\n" +
                "                    <ParmCode>Balance</ParmCode>\n" +
                "                    <Value>AVAILABLE</Value>\n" +
                "                </Parm>\n" +
                "            </ResultDtls>\n" +
                "            <ObjectFor>\n" +
                "                <ContractIDT>\n" +
                "                    <!-- contractNumber == clientNumber-->\n" +
                "                    <ContractNumber>" +
                                    clientNumber +
                                    "</ContractNumber>\n" +
                "                </ContractIDT>\n" +
                "            </ObjectFor>\n" +
                "        </Information>\n" +
                "    </MsgData>\n" +
                "</UFXMsg>";
        return  request;
    }

    private String BalanceResponseParse(String response){
        String balanceTemplate = "<Balances>" +
                                "<Balance>" +
                                "<Name>Available</Name>" +
                                "<Type>AVAILABLE</Type>.*?" +
                                "<Currency>USD</Currency>" +
                                "</Balance>";
        String balanceString = XMLParse.findValueInString(response, balanceTemplate);
        String amountTemplate = "<Amount>.*<\\/Amount>";
        String amount = XMLParse.findValueInString(balanceString, amountTemplate).
                replaceAll("[^0-9.\\s]", "");

        String currencyTemplate = "<Currency>.*<\\/Currency>";
        String currency = XMLParse.findValueInString(balanceString, currencyTemplate)
                .substring(10, 13);

        return amount + " "  + currency;
    }

    /**
     *
     * @param url destination address
     * @param request request for send
     * @return string with response content
     */
    private String SendRequest(String url, String request){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response;
        ResponseHandler <String> handler = new BasicResponseHandler();
        String responseString;

        try{
            StringEntity entityCreateClient = new StringEntity(request);
            httpPost.setEntity(entityCreateClient);
            httpPost.setHeader("Content-type", "text/xml");
            response = httpClient.execute(httpPost);
            HttpEntity ent = response.getEntity();

            responseString = new BufferedReader(new InputStreamReader(ent.getContent()))
                    .lines().collect(Collectors.joining("\n"));

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

        return responseString;
    }

    /**
     *
     * @param data data for generation
     * @return
     */
    private String GenerateId(String data){
        return  "XML_SSS_";
    }
}
