package openway.controller;

import openway.model.Client;
import openway.service.ClientService;
import openway.service.LesserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;


@RestController
public class MainController {

    private final static Logger logger = Logger.getLogger(MainController.class.getName());

    private final ClientService clientService;
    private final LesserService lesserService;

    public MainController(ClientService clientService, LesserService lesserService) {

        this.clientService = clientService;
        this.lesserService = lesserService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/data")
    List<Client> getClients() {
        logger.info("get clients data");
        return clientService.findAll();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/reg")
    boolean addNewLesser(@RequestBody String newItem) {
        logger.info("called addNewLesser()");
        try{
            lesserService.addNewLesser(newItem);
            logger.info("add new lessor");
            return true;
        }
        catch (Exception e){
            logger.info("error with saving data: not added new lessor");
            return false;
        }

    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    void getLoginPassword(@RequestBody String auth) {
        lesserService.setPasswordHash();
    }

//    @CrossOrigin(origins = "http://localhost:3000")
//    @PostMapping("/login")
//    boolean getLoginPassword(@RequestBody String auth) {
//        logger.info("check auth");
//        return lesserService.authentication(auth);
//    }

    @PostMapping("/moto")
    String newMoto(@RequestBody String newItem) {
        logger.info("motoooooooooo" + newItem);
        clientService.addNewClient(newItem);
        return "Hello  Daniil";
    }
}