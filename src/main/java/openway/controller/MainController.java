package openway.controller;

import openway.service.ClientService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class MainController {

    //private final ClientService clientService;

    public MainController(ClientService clientService) {

        //this.clientService = clientService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/clients")
    List<String> getCLient() {
        List<String> list = new ArrayList();
        list.add("qwqw");
        list.add("dsda");
        return list;
    }
}