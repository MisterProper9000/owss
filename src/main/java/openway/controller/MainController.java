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
            logger.info("add new lessor in database");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("error with saving data: not added new lessor");
            return false;
        }

    }


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    boolean getLoginPassword(@RequestBody String auth) {
        logger.info("check auth");
        logger.info("auth: " + lesserService.authentication(auth));
        return lesserService.authentication(auth);
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