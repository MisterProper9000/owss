package openway.controller;

import openway.model.Lesser;
import openway.service.LesserService;

import openway.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class MainController {

    private final static Logger logger = Logger.getLogger(MainController.class.getName());

    private final LesserService lesserService;
    private final OrderService orderService;

    public MainController(LesserService lesserService, OrderService orderService) {
        this.lesserService = lesserService;
        this.orderService = orderService;
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
    String getLoginPassword(@RequestBody String auth) {
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

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/lesserinfo")
    List<String> getLesInfo(@RequestBody String id) {
        logger.info("get lesser "+id);
        return lesserService.getNameSerName(id);
    }
}