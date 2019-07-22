package openway.controller;

import openway.model.Client;
import openway.service.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@RestController
public class MainController {

    private final static Logger logger = Logger.getLogger(MainController.class.getName());

    private final ClientService clientService;

    public MainController(ClientService clientService) {

        this.clientService = clientService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/data")
    List<Client> getClients() {
        logger.info("get clients data");
        return clientService.findAll();
    }
}