package openway.controller;

import openway.service.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;


@RestController
public class MobileController {

    private final static Logger logger = Logger.getLogger(MobileController.class.getName());

    private final ClientService clientService;

    public MobileController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/regclient")
    String addNewClient(@RequestBody String newItem) {
        logger.info("get moto registration data" + newItem);
        return clientService.addNewClient(newItem);
    }

    @PostMapping("/loginclient")
    String loginClient(@RequestBody String auth) {
        logger.info("called loginClient()" + auth);
        return clientService.authenticationClient(auth);
    }
}