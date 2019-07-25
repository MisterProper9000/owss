package openway.controller;

import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;


@RestController
public class ArduinoController {

    private final static Logger logger = Logger.getLogger(ArduinoController.class.getName());

    @GetMapping("/ard")
    String getForms() {
        logger.info("arduinoooo");
        return "Hello";
    }
}