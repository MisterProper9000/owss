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

    @GetMapping("/ardgetstatus")
    boolean sendStatus() {
        logger.info("moto status: " + motoService.getStatus(1));
        return motoService.getStatus(1);
    }

    @PostMapping("/ardqr")
    String checkQr(@RequestBody String qrAndEmail) {
        logger.info("get Qr-code: " + qrAndEmail);
        String status = motoService.checkQr(qrAndEmail);
        logger.info("status of checking qr: "+status);
        return "wait me now";
    }
}