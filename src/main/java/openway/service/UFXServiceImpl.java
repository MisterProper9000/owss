package openway.service;

import openway.model.Client;
import openway.model.Lesser;
import openway.utils.DateUfx;
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
import java.util.stream.Collectors;

@Service
public class UFXServiceImpl implements UFXService {

    private final static Logger logger = Logger.getLogger(UFXServiceImpl.class.getName());
    private String IssProductCode1 = "CLIENT_ISS_001";
    private String AcqProductCode1 = "LESSER_ASQ_001";
    private String urlUfxAdapter = "http://10.101.124.36:17777";
    private String depositSize = "200";
    private String depositCurrency = "USD";

    private String RRN;


    /**
     *
     * @param client class client with data for creating
     * @return string with result of 2 requests: create client iss + create contract iss
     */
    public String AddNewClientInWay4(Client client){

        String name = client.getFirst_name();
        String sName = client.getLast_name();

        String rnd = GenerateId("") + client.getId()+"CL";

        String clientNumber = rnd;
        String regNumberClient = rnd;
        String regNumberApp = rnd + "_A";
        String contractNumber = rnd;

        String requestCreateClient = RequestCreateClient(sName, name,
                clientNumber, regNumberClient);
        String requestCreateIssContract = RequestCreateIssContract(clientNumber,
                regNumberClient,  regNumberApp, contractNumber);

        logger.info("create client request: " + requestCreateClient);
        logger.info("create contract request: " + requestCreateIssContract);

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

        String rnd = GenerateId("") + lesser.getId()+"LES";
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

    public String BalanceRequestInWay4(int clientId){
        String clientNumber = GenerateId("") + clientId+"CL";
        String request = RequestCreateBalanceInquery(clientNumber);
        String response = SendRequest(urlUfxAdapter, request);
        logger.info("request: "+request+" , response: "+response);
        String balance = BalanceResponseParse(response);
        return balance;
    }

    public String GetDepositFromClient(int clientId, int lesserId){

        String clientNumber = GenerateId("") + clientId + "CL";
        String lesserNumber = GenerateId("") + lesserId + "LES";

        String requestGetDeposit = RequestCreateGetDeposit(clientNumber, lesserNumber,
                depositSize, depositCurrency);
        String resDeposit = SendRequest(urlUfxAdapter, requestGetDeposit);
        return resDeposit;
    }


    public String ReverseDeposit(int clientId, int lesserId, String RRN){

        String clientNumber = GenerateId("") + clientId + "CL";
        String lesserNumber = GenerateId("") + lesserId + "LES";

        String requestReverseDeposit = RequestCreateReverseDeposit(clientNumber, lesserNumber,
                RRN, depositSize, depositCurrency);

        String resReverseDeposit = SendRequest(urlUfxAdapter, requestReverseDeposit);
        return resReverseDeposit;
    }

    public String GetPayment(int clientId, int lesserId, float cost){
        String clientNumber = GenerateId("") + clientId + "CL";
        String lesserNumber = GenerateId("") + lesserId + "LES";
        String sum = Float.toString(cost);

        String requestPayment = RequestCreatePayment(clientNumber, lesserNumber,
                sum, depositCurrency);

        String resRequestPayment = SendRequest(urlUfxAdapter, requestPayment);
        return resRequestPayment;
        //return "";
    }

    public  String GetRrn(){
        return this.RRN;
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

        return "<UFXMsg scheme=\"WAY4Appl\" msg_type=\"Application\" version=\"2.0\" direction=\"Rq\">\n" +
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

        return  "<UFXMsg scheme=\"WAY4Appl\" msg_type=\"Application\" version=\"2.0\" direction=\"Rq\">\n" +
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

        return  "<UFXMsg scheme=\"WAY4Appl\" msg_type=\"Application\" version=\"2.0\" direction=\"Rq\">\n" +
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
    }


    private String RequestCreateBalanceInquery(String clientNumber){
        String regNumberApp = clientNumber + "_B";
        return  "<UFXMsg direction=\"Rq\" msg_type=\"Information\" scheme=\"WAY4Appl\" version=\"2.0\">\n" +
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

    private String RequestCreateGetDeposit(String clientNumber,
                                           String lesserNumber,
                                           String sum, String currency){

        String RRN = GenerateRRN("time");
        String dateStr = DateUfx.getTime();
        this.RRN = RRN;

        return  "<UFXMsg direction=\"Rq\" msg_type=\"Doc\" scheme=\"WAY4Doc\" version=\"2.0\">\n" +
                "    <MsgId>AAA-555-333-EEE-23124141</MsgId>\n" +
                "    <Source app=\"MobileApp\"/>\n" +
                "    <MsgData>\n" +
                "        <Doc>\n" +
                "            <TransType>\n" +
                "                <TransCode>\n" +
                "                    <MsgCode>auth_purchase</MsgCode>\n" +
                "                </TransCode>\n" +
                "                <TransCondition>AUTHENTICATED,AUTH_AGENT,CARD,CARDHOLDER,DATA_TRACK,MERCH,PBT,PBT_CRYPTO,PBT_ONLINE,READ_TRACK,TERM,TERM_TRACK</TransCondition>\n" +
                "            </TransType>\n" +
                "            <DocRefSet>\n" +
                "                <Parm>\n" +
                "                    <ParmCode>RRN</ParmCode>\n" +
                "                    <Value>" +
                                    RRN +
                                    "</Value>\n" +
                "                </Parm>\n" +
                "            </DocRefSet>\n" +
                "            <LocalDt>" +
                            dateStr +
                            "</LocalDt>\n" +
                "            <Requestor>\n" +
                "                <ContractNumber>" +
                                clientNumber +
                                "</ContractNumber>\n" +
                "                <MemberId>0001</MemberId>\n" +
                "            </Requestor>\n" +
                "            <Source>\n" +
                "                <ContractNumber>" +
                                lesserNumber +
                                "</ContractNumber>\n" +
                "            </Source>       \n" +
                "            <Transaction>\n" +
                "                <Currency>" +
                                currency +
                                "</Currency>\n" +
                "                <Amount>" +
                                sum +
                                "</Amount>\n" +
                "            </Transaction>\n" +
                "        </Doc>\n" +
                "    </MsgData>\n" +
                "</UFXMsg>";
    }

    private String RequestCreateReverseDeposit(String clientNumber,
                                               String lesserNumber,
                                               String RRN,
                                               String sum, String currency) {
        String dateStr = DateUfx.getTime();
        return  "<UFXMsg direction=\"Rq\" msg_type=\"Doc\" scheme=\"WAY4Doc\" version=\"2.0\">\n" +
                "    <MsgId>AAA-555-333-EEE-23124141</MsgId>\n" +
                "    <Source app=\"MobileApp\"/>\n" +
                "    <MsgData>\n" +
                "        <Doc>\n" +
                "            <TransType>\n" +
                "                <TransCode>\n" +
                "                    <MsgCode>auth_purchase_rev</MsgCode>\n" +
                "                </TransCode>\n" +
                "                <TransCondition>AUTHENTICATED,AUTH_AGENT,CARD,CARDHOLDER,DATA_TRACK,MERCH,PBT,PBT_CRYPTO,PBT_ONLINE,READ_TRACK,TERM,TERM_TRACK</TransCondition>\n" +
                "            </TransType>\n" +
                "            <DocRefSet>\n" +
                "                <Parm>\n" +
                "                    <ParmCode>RRN</ParmCode>\n" +
                "                    <!-- rrn == date in format yymmddHHmmss-->\n" +
                "                    <!-- rrn == rrn from getDeposit-->\n" +
                "                    <Value>" +
                                    RRN +
                                    "</Value>\n" +
                "                </Parm>\n" +
                "            </DocRefSet>\n" +
                "            <LocalDt>" +
                            dateStr +
                            "</LocalDt>\n" +
                "            <Requestor>\n" +
                "                <ContractNumber>" +
                                clientNumber +
                                "</ContractNumber>\n" +
                "                <MemberId>0001</MemberId>\n" +
                "            </Requestor>\n" +
                "            <Source>\n" +
                "                <ContractNumber>" +
                                lesserNumber +
                                "</ContractNumber>\n" +
                "            </Source>\n" +
                "            <Transaction>\n" +
                "                <Currency>" +
                                currency +
                                "</Currency>\n" +
                "                <Amount>" +
                                sum +
                                "</Amount>\n" +
                "            </Transaction>\n" +
                "        </Doc>\n" +
                "    </MsgData>\n" +
                "</UFXMsg>";
    }

    private String RequestCreatePayment(String clientNumber,
                                        String lesserNumber,
                                        String sum, String currency){

        String RRN = GenerateRRN("time");
        String dateStr = DateUfx.getTime();
        return  "<UFXMsg direction=\"Rq\" msg_type=\"Doc\" scheme=\"WAY4Doc\" version=\"2.0\">\n" +
                "    <MsgId>AAA-555-333-EEE-23124141</MsgId>\n" +
                "    <Source app=\"MobileApp\"/>\n" +
                "    <MsgData>\n" +
                "        <Doc>\n" +
                "            <TransType>\n" +
                "                <TransCode>\n" +
                "                    <MsgCode>02000R</MsgCode>\n" +
                "                </TransCode>\n" +
                "                <TransCondition>AUTHENTICATED,AUTH_AGENT,CARD,CARDHOLDER,DATA_TRACK,MERCH,PBT,PBT_CRYPTO,PBT_ONLINE,READ_TRACK,TERM,TERM_TRACK</TransCondition>\n" +
                "            </TransType>\n" +
                "            <DocRefSet>\n" +
                "                <Parm>\n" +
                "                    <ParmCode>RRN</ParmCode>\n" +
                "                    <Value>" +
                                    RRN +
                                    "</Value>\n" +
                "                </Parm>\n" +
                "            </DocRefSet>\n" +
                "            <LocalDt>" +
                            dateStr +
                            "</LocalDt>\n" +
                "            <Requestor>\n" +
                "                <ContractNumber>" +
                                clientNumber +
                                "</ContractNumber>\n" +
                "                <MemberId>0001</MemberId>\n" +
                "            </Requestor>\n" +
                "            <Source>\n" +
                "                <ContractNumber>" +
                                lesserNumber +
                                "</ContractNumber>\n" +
                "            </Source>\n" +
                "            <Transaction>\n" +
                "                <Currency>" +
                                currency +
                                "</Currency>\n" +
                "                <Amount>" +
                                sum +
                                "</Amount>\n" +
                "            </Transaction>\n" +
                "        </Doc>\n" +
                "    </MsgData>\n" +
                "</UFXMsg>";
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
        return  "XML_FF_";
    }

    /**
     *
     * @param type type of data for generating RRN
     *             time => RRN == date in format yyMMddHHmmss
     * @return
     */
    private String GenerateRRN(String type){
        String res = "";
        if(type.equals("time"))
        {
            SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
            return format.format(new Date());
        }
        return res;
    }
}