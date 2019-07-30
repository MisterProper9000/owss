package openway.controller;

import openway.service.MotoService;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;


@RestController
public class MotoController {

    private final static Logger logger = Logger.getLogger(MotoController.class.getName());

    private final MotoService motoService;

    public MotoController(MotoService motoService) {
        this.motoService = motoService;
    }

    @GetMapping("/ardget")
    boolean getForms() {
        logger.info("moto status: " + motoService.getStatus(1));
        return motoService.getStatus(1);
    }

    @PostMapping("/ardpost")
    void getForms(@RequestBody String newItem) {
        logger.info("arduinoooo" + newItem);
    }
}