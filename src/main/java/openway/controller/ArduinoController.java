package openway.controller;

import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;


@RestController
public class ArduinoController {

    private final static Logger logger = Logger.getLogger(MainController.class.getName());

    @PostMapping("/ard")
    void getForms(@RequestBody String newItem) {
        logger.info("arduinoooo"+newItem);
    }
}