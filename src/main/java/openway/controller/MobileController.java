package openway.controller;

import openway.model.Client;
import openway.service.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;


@RestController
public class MobileController {

    private final static Logger logger = Logger.getLogger(MainController.class.getName());

    private final ClientService clientService;

    public MobileController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/moto")
    String newMoto(@RequestBody String newItem) {
        logger.info("get moto registration data" + newItem);
        clientService.addNewClient(newItem);
        return "Hello  Daniil";
    }

}