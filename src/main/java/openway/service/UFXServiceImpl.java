package openway.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UFXServiceImpl implements UFXService{

    public String requestCreateClient(String sName, String name,
                                      int clientNumber, String email,
                                      String regNumber){

        String clientNumberStr = Integer.toString(clientNumber);

        String res = "<UFXMsg scheme=\"WAY4Appl\" msg_type=\"Application\" " +
                    "version=\"2.0\" direction=\"Rq\">\n" +
                    "    <MsgId>AAA-555-333-EEE-23124141</MsgId>\n" +
                    "    <Source app=\"MobileApp\"/> \n    <MsgData>\n" +
                    "        <Application>\n" +
                    "            <RegNumber>" +
                                    regNumber +
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
                                             clientNumberStr +
                                             "</ClientNumber>\n" +
                    "                        <RegNumberType>RegNumberType</RegNumberType>\n" +
                    "                        <RegNumber>" +
                                             clientNumberStr +
                                             "</RegNumber>\n" +
                    "                        <RegNumberDetails>RegDetails</RegNumberDetails>\n" +
                    "                        <FirstName>" +
                                             name +
                                             "</FirstName>\n" +
                    "                        <LastName>" +
                                             sName +
                                             "</LastName>\n" +
                    "                    </ClientInfo>\n\n" +
                    "                    <AddInfo>\n" +
                    "                        <AddInfo01>add_info_1_3456789_12345678</AddInfo01>\n" +
                    "                        <AddInfo02>add_info_2_3456789_12345678</AddInfo02>\n" +
                    "                        <AddDate01>1981-08-13</AddDate01>\n" +
                    "                        <AddDate02>1985-06-12</AddDate02>\n" +
                    "                        <AnyTagClient>AnyTagClientValue</AnyTagClient>\n" +
                    "                        <SecondAnyTagClient>SecondAnyTagClientValue</SecondAnyTagClient>\n" +
                    "                    </AddInfo>\n" +
                    "                </Client>\n" +
                    "            </Data>\n" +
                    "        </Application>\n" +
                    "\t</MsgData>\n" +
                    "</UFXMsg>";
        return res;
    }

    public String requestCreateIssContract(String sName, String name,
                                    int clientNumber, String email,
                                    String regNumber){
        return "";
    }

}
