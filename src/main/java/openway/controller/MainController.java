package openway.controller;

import openway.model.Lesser;
import openway.service.LesserService;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;


@RestController
public class MainController {

    private final static Logger logger = Logger.getLogger(MainController.class.getName());

    private final LesserService lesserService;

    public MainController(LesserService lesserService) {

        this.lesserService = lesserService;
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

//    @CrossOrigin(origins = "http://localhost:3000")
//    @PostMapping("/login")
//    void getLoginPassword(@RequestBody String auth) {
//        lesserService.setPasswordHash();
//    }

    @CrossOrigin(origins = "http://10.101.177.21:9091:3000")
    @PostMapping("/login")
    Lesser getLoginPassword(@RequestBody String auth) {
        logger.info("check auth");
        return lesserService.authentication(auth);
    }
}