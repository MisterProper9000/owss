package openway.controller;

import openway.model.Lesser;
import openway.service.LesserService;
import openway.service.UFXService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;


@RestController
public class MainController {

    private final static Logger logger = Logger.getLogger(MainController.class.getName());

    private final LesserService lesserService;
    private final UFXService ufxService;

    public MainController(LesserService lesserService, UFXService ufxService) {

        this.lesserService = lesserService;
        this.ufxService = ufxService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/reg")
    boolean addNewLesser(@RequestBody String newItem) {
        logger.info("called addNewLesser()");
        try {
            lesserService.addNewLesser(newItem);
            logger.info("add new lessor");
            return true;
        } catch (Exception e) {
            logger.info("error with saving data: not added new lessor");
            return false;
        }

    }

    String s = new String("<UFXMsg scheme=\"WAY4Appl\" msg_type=\"Application\" version=\"2.0\" direction=\"Rq\">\n" +
            "    <MsgId>AAA-555-333-EEE-23124141</MsgId>\n" +
            "    <Source app=\"MobileApp\"/>\n" +
            "    <MsgData>\n" +
            "        <Application>\n" +
            "            <RegNumber>tfrwygsvah</RegNumber>\n" +
            "\t\t\t<Institution>0001</Institution>\n" +
            "            <OrderDprt>0101</OrderDprt>\n" +
            "            <ObjectType>Client</ObjectType>\n" +
            "            <ActionType>Add</ActionType>\n" +
            "            <Data>\n" +
            "                <Client>\n" +
            "                    <ClientType>PR</ClientType>\n" +
            "                    <ClientInfo>\n" +
            "                        <ClientNumber>123</ClientNumber>\n" +
            "                        <RegNumberType>RegNumberType</RegNumberType>\n" +
            "                        <RegNumber>123</RegNumber>\n" +
            "                        <RegNumberDetails>RegDetails</RegNumberDetails>\n" +
            "                        <FirstName>Vasa</FirstName>\n" +
            "                        <LastName>Vasil</LastName>\n" +
            "                    </ClientInfo>\n" +
            "\n" +
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
            "</UFXMsg>");

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    String getLoginPassword(@RequestBody String auth) {
        logger.info("check auth");
        logger.info("prooooooooooooooooob   "+lesserService.authentication(auth));
        //goToWay4(s);
        return lesserService.authentication(auth);
    }

    @CrossOrigin(origins = "http://10.101.124.36:17777")
    @PostMapping()
    String goToWay4(@RequestBody String s) {
        logger.info("way4test" + s);
        return s;
    }

//    void way() {
//        HttpClient client = HttpClient.newBuilder().build();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(uri))
//                .POST(BodyPublishers.ofString(data))
//                .build();
//
//        HttpResponse<?> response = client.send(request, BodyHandlers.discarding());
//        System.out.println(response.statusCode());
//    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/info")
    List<Lesser> getForms() {
        logger.info("get all info about applications (called findAll())");
        return lesserService.findAll();
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/infolistid")
    List<Integer> getListOfId() {
        logger.info("get list of appliers id (called listOfIdClients())");
        return lesserService.listofidlessers();
    }
}