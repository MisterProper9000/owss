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
    void newLesser(@RequestBody String newItem) {
        logger.info("add lesser to db");
        lesserService.addNewLesser(newItem);
    }
}