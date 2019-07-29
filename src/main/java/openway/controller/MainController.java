package openway.controller;

import openway.model.Lesser;
import openway.service.LesserService;
import openway.service.UFXService;
import openway.service.UFXServiceImpl;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.web.bind.annotation.*;
import org.apache.http.impl.client.HttpClients;
import sun.net.www.http.HttpClient;

import javax.xml.soap.Name;
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

            UFXService ufxSer = new UFXServiceImpl();
            String tst_sName = "tstFromServer_sName";
            String tst_Name = "tstFromServer_Name";

            String rnd = ufxSer.GenerateId("kek");

            String tst_clientNumber = rnd;
            String tst_regNumberClient = rnd;
            String tst_regNumberApp = rnd + "_A";
            String tst_conractNumber = rnd;

            String urlUfxAdapter = "http://10.101.124.36:17777";

            String requestCreateClient = ufxSer.RequestCreateClient(tst_sName, tst_Name,
                    tst_clientNumber, tst_regNumberClient);
            String requestCreateIssContract = ufxSer.RequestCreateIssContract(tst_clientNumber,
                    tst_regNumberClient,  tst_regNumberApp, tst_conractNumber);

            logger.info("create client request: " + requestCreateClient);
            logger.info("create client request: " + requestCreateIssContract);

            String res = ufxSer.SendRequest(urlUfxAdapter, requestCreateClient);
            logger.info(res);
            res = ufxSer.SendRequest(urlUfxAdapter, requestCreateIssContract);
            logger.info(res);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("error with saving data: not added new lessor");
            return false;
        }

    }

    private String SentToWay4(String request) {



        return "";
    }



    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    String getLoginPassword(@RequestBody String auth) {
        logger.info("check auth");
        logger.info("prooooooooooooooooob   " + lesserService.authentication(auth));
        //goToWay4(s);
        return lesserService.authentication(auth);
    }

    @CrossOrigin(origins = "http://10.101.124.36:17777")
    @PostMapping()
    String goToWay4(@RequestBody String s) {
        logger.info("way4test" + s);
        return s;
    }

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