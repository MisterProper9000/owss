package openway.service;

import openway.model.Client;
import openway.model.Lesser;
import openway.utils.DateUfx;
import openway.utils.XMLParse;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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

    private enum cTypes {LesserT, ClientT};

    private String RRN;
    private String VPosId = "SOA0001";

    private String adrStringTrans = "            <SourceDtls> \n" +
            "               <SIC>6012</SIC>\n" +
            "               <Country>RU</Country> \n" +
            "               <City>Merchant City</City>\n" +
            "               <MerchantName>Merchant Name</MerchantName>\n" +
            "            </SourceDtls>\n";

    private String adrStringAcq = "                        <BaseAddress>\n" +
            "                            <EMail>email_acq@acqmail.ru</EMail>\n" +
            "                            <City>Moscow</City>\n" +
            "                            <PostalCode>00899334</PostalCode>\n" +
            "                            <AddressLine1>smth</AddressLine1>\n" +
            "                            <AddressLine2>smth</AddressLine2>\n" +
            "                            <ActivityPeriod>\n" +
            "                                <DateFrom>1970-01-25</DateFrom>\n" +
            "                                <DateTo>2020-08-20</DateTo>\n" +
            "                            </ActivityPeriod>\n" +
            "                        </BaseAddress>";


    /**
     *
     * @param client class client with data for creating
     * @return string with result of 2 requests: create client iss + create contract iss
     */
    public String AddNewClientInWay4(Client client){

        String name = client.getFirst_name();
        String sName = client.getLast_name();

        String rnd = GenerateId(client.getId() + "CL");

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

        String rnd = GenerateId(lesser.getId() + "LES");

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

    public String BalanceClientRequestInWay4(int clientId){
        String clientNumber = GenerateId( clientId + "CL");
        String request = RequestCreateBalanceInquery(clientNumber, cTypes.ClientT);
        logger.info("way4 data for client balance request: " + clientNumber);
        String response = SendRequest(urlUfxAdapter, request);
        String balance = BalanceResponseParse(response, cTypes.ClientT);
        logger.info("balance client response parsed result: " + balance);
        return balance;
    }

    public String BalanceLesserRequestInWay4(int lesserId){
        String lesserNumber = GenerateId(lesserId + "LES");
        String request = RequestCreateBalanceInquery(lesserNumber, cTypes.LesserT);
        logger.info("way4 data for lesser balance request: " + lesserNumber);
        String response = SendRequest(urlUfxAdapter, request);
        String balance = BalanceResponseParse(response, cTypes.LesserT);
        logger.info("balance lesser response parsed result: " + balance);
        return balance;
    }

    public String GetDepositFromClient(int clientId, int lesserId){
        String clientNumber = GenerateId(clientId + "CL");
        String lesserNumber = GenerateId(lesserId + "LES");
        String requestGetDeposit = RequestCreateGetDeposit(clientNumber, lesserNumber,
                depositSize, depositCurrency);
        String resDeposit = SendRequest(urlUfxAdapter, requestGetDeposit);
        return resDeposit;
    }

    public String ReverseDeposit(int clientId, int lesserId, String RRN){
        String clientNumber = GenerateId(clientId + "CL");
        String lesserNumber = GenerateId(lesserId + "LES");
        String requestReverseDeposit = RequestCreateReverseDeposit(clientNumber, lesserNumber,
                RRN, depositSize, depositCurrency);
        String resReverseDeposit = SendRequest(urlUfxAdapter, requestReverseDeposit);
        return resReverseDeposit;
    }

    public String GetPayment(int clientId, int lesserId, float cost){
        String clientNumber = GenerateId(clientId + "CL");
        String lesserNumber = GenerateId(lesserId + "LES");
        String sum = Float.toString(cost);
        String requestPayment = RequestCreatePayment(clientNumber, lesserNumber,
                sum, depositCurrency);
        String resRequestPayment = SendRequest(urlUfxAdapter, requestPayment);
        return resRequestPayment;
    }

    public  String GetRrn(){
        return this.RRN;
    }

    public String CheckRes(String str){
        String sp = "Successfully processed";

        String res = XMLParse.findValueInString(str, sp);
        if(!res.equals(sp)){
            return String.valueOf(Status.ERROR);
        }
        return String.valueOf(Status.OK);
    }

    private String checkTopUp(String response){
        String sp = "Success";

        String res = XMLParse.findValueInString(response, sp);
        if(!res.equals(sp)){
            return Status.ERROR + "|";
        }
        return Status.OK + "|";
    }

    public String ClientTopUp(String name, String sName, String cardNum,
                              String cvc2, String exDate,String amount, int clientId){
        return TopUp(name, sName, cardNum,
                cvc2, exDate, amount,
                clientId, cTypes.ClientT);
    }

    public String LesserTopUp(String name, String sName, String cardNum,
                              String cvc2, String exDate,String amount, int lesserId){
        return TopUp(name, sName, cardNum,
                cvc2, exDate, amount,
                lesserId, cTypes.LesserT);
    }

    private String TopUp(String name, String sName, String cardNum,
                         String cvc2, String exDate,String amount,
                         int Id, cTypes type){

        String clientNumber = "";
        switch (type){
            case LesserT:
            {
                clientNumber = GenerateId(Id + "LES");
                break;
            }
            default:
            {
                clientNumber = GenerateId(Id + "CL");
                break;
            }
        }
        cardNum = cardNum.replaceAll("-", "");
        exDate = exDate.replaceAll("\\/", "");
        String topUpRequest = RequestCreateTopUpCl(name, sName, cardNum,
                cvc2, exDate, amount, clientNumber);
        String resTopUp = SendRequest(urlUfxAdapter, topUpRequest);
        logger.info(resTopUp);
        String redResTopUp = checkTopUp(resTopUp);
        return redResTopUp;

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
                "    <MsgId>AAA-555-333-EEE-23124145</MsgId>\n" +
                "    <Source app=\"MobileApp\"/>\n" +
                "    <MsgData>\n" +
                "        <Application>\n" +
                "            <RegNumber>" +
                            regNumberApp +
                            "</RegNumber>\n" +
                "            <Institution>0001</Institution>\n" +
                "            <OrderDprt>0101</OrderDprt>\n" +
                "            <ObjectType>Client</ObjectType>\n" +
                "            <ActionType>Add</ActionType>\n" +
                "            <Data>\n" +
                "                <Client>\n" +
                "                    <ClientType>PR</ClientType>\n" +
                "                    <ClientInfo>\n" +
                "                        <!-- clientNumber == regNumberApp without \"_A\" -->\n" +
                "                        <ClientNumber>" +
                                        clientNumber +
                                        "</ClientNumber>\n" +
                "                        <!-- regNumberClient == clientNumber -->\n" +
                "                        <RegNumber>" +
                                        clientNumber +
                                        "</RegNumber>\n" +
                "                        <RegNumberDetails>RegDetails</RegNumberDetails>\n" +
                "                        <FirstName>" +
                                        name +
                                        "</FirstName>\n" +
                "                        <LastName>" +
                                        sName +
                                        "</LastName>\n" +
                "                        <Country>RUS</Country>\n" +
                "                    </ClientInfo>\n" +
                "                    <BaseAddress>\n" +
                "                        <!-- this is not clients email, it's for card-wallet operations\n" +
                "                        all data in base address is fake-->\n" +
                "                        <EMail>fake@email.com</EMail>\n" +
                "                        <Country>RUS</Country>\n" +
                "                        <City>Petersburg</City>\n" +
                "                        <PostalCode>00334</PostalCode>\n" +
                "                        <AddressLine1>1234523454567890_1</AddressLine1>\n" +
                "                        <AddressLine2>1234523456789340_1</AddressLine2>\n" +
                "                        <ActivityPeriod>\n" +
                "                            <DateFrom>2010-08-20</DateFrom>\n" +
                "                            <DateTo>2020-08-21</DateTo>\n" +
                "                        </ActivityPeriod>\n" +
                "                    </BaseAddress>\n" +
                "                </Client>\n" +
                "            </Data>\n" +
                "        </Application>\n" +
                "    </MsgData>\n" +
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
                "                            <City>Moscow</City>\n" +
                "                            <PostalCode>00899334</PostalCode>\n" +
                "                            <AddressLine1>smth</AddressLine1>\n" +
                "                            <AddressLine2>smth</AddressLine2>\n" +
                "                            <ActivityPeriod>\n" +
                "                                <DateFrom>1970-01-25</DateFrom>\n" +
                "                                <DateTo>2020-08-20</DateTo>\n" +
                "                            </ActivityPeriod>\n" +
                "                        </BaseAddress>"
                +
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


    private String RequestCreateBalanceInquery(String clientNumber, cTypes type){
        String regNumberApp = clientNumber + "_B";
        String balanceType = "";
        switch (type){
            case LesserT:
                balanceType = "ACQ_AVAILABLE";
                break;
            default:
                balanceType = "AVAILABLE";
                break;
        }
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
                "                    <Value>" +
                balanceType +
                "</Value>\n" +
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

    private String BalanceResponseParse(String response, cTypes type){
        String balanceType = "";
        switch (type){
            case LesserT:
                balanceType = "<Name>Acquiring Balance</Name>" +
                        "<Type>ACQ_AVAILABLE</Type>";
                break;
            default:
                balanceType = "<Name>Available</Name><Type>AVAILABLE</Type>";
                break;
        }


        String balanceTemplate = "<Balances>" +
                                "<Balance>" + balanceType + ".*?" +
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

    private String RequestCreateTopUpCl(String name, String sName, String cardNum,
                                        String cvc2, String exDate, String amount,
                                        String clientNumber){
        String dateStr = DateUfx.getTime();
        return "<UFXMsg direction=\"Rq\" msg_type=\"Doc\" scheme=\"WAY4Doc\" version=\"2.0\">\n" +
                "    <MsgId>1211372852124</MsgId>\n" +
                "    <Source app=\"mobileApp\"/>\n" +
                "    <MsgData>\n" +
                "        <Doc>\n" +
                "            <TransType>\n" +
                "                <TransCode>\n" +
                "                    <MsgCode>OrdReq</MsgCode>\n" +
                "                    <ServiceCode>P2P</ServiceCode>\n" +
                "                </TransCode>\n" +
                "                <TransCondition>MBSMS</TransCondition>\n" +
                "                <TransRules>\n" +
                "                    <TransRule>\n" +
                "                        <ParmCode>CreditTransCondition</ParmCode>\n" +
                "                        <Value>TBAPPL,MNET,ENET,NO_CARD,NO_TERM</Value>\n" +
                "                    </TransRule>\n" +
                "                    <TransRule>\n" +
                "                        <ParmCode>BusinessApplicationIdentifier</ParmCode>\n" +
                "                        <Value>MP</Value>\n" +
                "                    </TransRule>\n" +
                "                </TransRules>\n" +
                "            </TransType>\n" +
                "            <LocalDt>" +
                            dateStr +
                            "</LocalDt>\n" +
                            adrStringTrans +

                "            <Requestor>\n" +
                "                <ContractNumber>" +
                                cardNum +
                                "</ContractNumber> " +
                "<!-- Put your test card number (sender) here -->\n" +
                "                <CardInfo>\n" +
                "                    <CardExpiry>" +
                                    exDate +
                                    "</CardExpiry> " +
                "<!-- Put your test card (sender) expiration date in format YYMM here -->\n" +
                "                </CardInfo>\n" +
                "                <SecurityData>\n" +
                "                    <SecParm>\n" +
                "                        <Code>CVV2</Code>\n" +
                "                        <Value>" +
                                        cvc2 +
                                        "</Value> " +
                "<!-- Put your test card (sender) CVV2 here -->\n" +
                "                    </SecParm>\n" +
                "                </SecurityData>\n" +
                "            </Requestor>\n" +
                "            <Source>\n" +
                "                <ContractNumber>" +
                                VPosId +
                                "</ContractNumber> " +
                "<!-- Put your virtual POS terminal ID here -->\n" +
                "            </Source>\n" +
                "            <Destination>\n" +
                "                <ContractNumber>" +
                                clientNumber +
                                "</ContractNumber> " +
                "<!-- Put your test card number (receive) here -->\n" +
                "            </Destination>\n" +
                "            <Transaction>\n" +
                "                <Currency>" +
                                depositCurrency +
                                "</Currency>\n" +
                "                <Amount>" +
                                amount +
                                "</Amount>\n" +
                "            </Transaction>\n" +
                "        </Doc>\n" +
                "    </MsgData>\n" +
                "</UFXMsg>";
    }


    public String ClientDownMoney(String cardNum, String cvc2,
                           String exDate,String amount, int clientId){

        String clientNumber = GenerateId(clientId + "CL");
        String cardNumReg = cardNum.replaceAll("-", "");
        String exDateReg = exDate.replaceAll("\\/", "");
        String request = RequestCreateDownMoneyClient(cardNumReg, cvc2, exDateReg,
                amount, clientNumber);
        logger.info("request for client down money: " + request);
        String resDownMoney = SendRequest(urlUfxAdapter, request);
        logger.info("response down money: " + resDownMoney);
        String redResDownMoney = checkTopUp(resDownMoney);
        return redResDownMoney;
    }


    private String RequestCreateDownMoneyClient(String cardNum, String cvc2,
                                         String exDate, String amount,
                                         String clientNumber){
        String dateStr = DateUfx.getTime();
        return "<UFXMsg direction=\"Rq\" msg_type=\"Doc\" scheme=\"WAY4Doc\" version=\"2.0\">\n" +
                "  <MsgId>1211372852124</MsgId>\n" +
                "  <Source app=\"mobileApp\"/>\n" +
                "  <MsgData>\n" +
                "    <Doc>\n" +
                "      <TransType>\n" +
                "        <TransCode>\n" +
                "          <MsgCode>OrdReq</MsgCode>\n" +
                "          <ServiceCode>P2P</ServiceCode>\n" +
                "        </TransCode>\n" +
                "        <TransCondition>MBSMS</TransCondition>\n" +
                "        <TransRules>\n" +
                "          <TransRule>\n" +
                "            <ParmCode>CreditTransCondition</ParmCode>\n" +
                "            <Value>,TBAPPL,MNET,ENET,NO_CARD,NO_TERM,</Value>\n" +
                "          </TransRule>\n" +
                "          <TransRule>\n" +
                "            <ParmCode>BusinessApplicationIdentifier</ParmCode>\n" +
                "            <Value>MP</Value>\n" +
                "          </TransRule>\n" +
                "        </TransRules>\n" +
                "      </TransType>\n" +
                "      <LocalDt>" +
                        dateStr +
                        "</LocalDt>\n" +
                "      <SourceDtls>\n" +
                "        <SIC>6012</SIC>\n" +
                "        <Country>RU</Country>\n" +
                "        <City>Merchant City</City>\n" +
                "        <MerchantName>Merchant Name</MerchantName>\n" +
                "      </SourceDtls>\n" +
                "      <Requestor>\n" +
                "      \t<ContractNumber>" +
                        clientNumber +
                        "</ContractNumber> \n" +
                "      </Requestor>\n" +
                "      <Source>\n" +
                "        <ContractNumber>" +
                        VPosId +
                        "</ContractNumber> <!-- Put your virtual POS terminal ID here -->\n" +
                "      </Source>\n" +
                "      <Destination>\n" +
                "      \t<ContractNumber>" +
                        cardNum +
                        "</ContractNumber> <!-- Put your test card number (sender) here -->\n" +
                "        <CardInfo>\n" +
                "          <CardExpiry>" +
                            exDate +
                            "</CardExpiry> <!-- Put your test card (sender) expiration date in format YYMM here -->\n" +
                "        </CardInfo>\n" +
                "       <SecurityData>\n" +
                "         <SecParm>\n" +
                "           <Code>CVV2</Code>\n" +
                "           <Value>" +
                            cvc2 +
                            "</Value> <!-- Put your test card (sender) CVV2 here -->\n" +
                "         </SecParm>\n" +
                "       </SecurityData><!-- Put your test card number (receive) here -->\n" +
                "      </Destination>\n" +
                "      <Transaction>\n" +
                "        <Currency>" +
                        depositCurrency +
                        "</Currency>\n" +
                "        <Amount>" +
                        amount +
                        "</Amount>\n" +
                "        <Extra>\n" +
                "          <Type>CustomData</Type>\n" +
                "          <Details>E82DC229U0100000000007360500100000000007360501111</Details>\n" +
                "        </Extra>\n" +
                "      </Transaction>\n" +
                "    </Doc>\n" +
                "  </MsgData>\n" +
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
        return  "XML_XX_" + data;
    }

    /**
     *
     * @param type type of data for generating RRN
     *             time => RRN == date in format yyMMddHHmmss
     * @return
     */
    public String GenerateRRN(String type){
        String res = "";
        if(type.equals("time"))
        {
            SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
            return format.format(new Date());
        }
        return res;
    }

    @Override
    public double getDepositSize() {
        return Double.valueOf(depositSize);
    }

    public String LesserUpdMotoInfo(int lesserId, int newSumMoto ,String cardNum){
        String lesserNumber = GenerateId(lesserId + "LES");
        String newSumMotoStr = String.valueOf(newSumMoto);
        String request = RequestCreateUpdMoto(lesserNumber, newSumMotoStr, cardNum);
        String resUpd = SendRequest(urlUfxAdapter, request);
        return resUpd;
    }

    private String RequestCreateUpdMoto(String lesserNumber,
                                        String newSum, String cardNum ){
        String regNumberApp = lesserNumber + "_U";
        String spcSumMoto = "ACQ_MOTO" + newSum + ";";
        String spcContrNum = "CONTRNUM=" + cardNum + ";";


        return  "<UFXMsg direction=\"Rq\" msg_type=\"Doc\" scheme=\"WAY4Doc\" version=\"2.0\">\n" +
                "    <MsgId>1211372852124</MsgId>\n" +
                "    <Source app=\"mobApp\"/>\n" +
                "    <MsgData>\n" +
                "        <Application>\n" +
                "            <RegNumber>" +
                            "XML_" + GenerateRRN("time") +
                            "</RegNumber>\n" +
                "            <Institution>0001</Institution>\n" +
                "            <OrderDprt>0101</OrderDprt>\n" +
                "            <ObjectType>Contract</ObjectType>\n" +
                "            <ActionType>Update</ActionType>\n" +
                "            <ObjectFor>\n" +
                "                <ContractIDT>\n" +
                "                    <ContractNumber>" +
                                    lesserNumber +
                                    "</ContractNumber>\n" +
                "                    <Client>\n" +
                "                        <ClientInfo>\n" +
                "                            <RegNumber>" +
                                            lesserNumber +
                                            "</RegNumber>\n" +
                "                        </ClientInfo>\n" +
                "                    </Client>\n" +
                "                </ContractIDT>\n" +
                "            </ObjectFor>\n" +
                "            <Data>\n" +
                "                <Contract>\n" +
                "                    <ContractIDT>\n" +
                "                        <ContractNumber>" +
                                        lesserNumber +
                                        "</ContractNumber>\n" +
                "                    </ContractIDT>\n" +
                "                    <ContractName>" +
                                    lesserNumber + "_N" +
                                    "</ContractName>\n" +
                "                    <AddContractInfo>\n" +
                "                        <AddInfo01>" +
                                        spcSumMoto +
                                        spcContrNum +
                                        "</AddInfo01>\n" +
                "                    </AddContractInfo>\n" +
                "                </Contract>\n" +
                "            </Data>\n" +
                "        </Application>\n" +
                "    </MsgData>\n" +
                "</UFXMsg>";
    }
}