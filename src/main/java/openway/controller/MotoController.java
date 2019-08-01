package openway.controller;

import openway.model.Motoroller;
import openway.service.MotoService;
import openway.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class MotoController {

    private final static Logger logger = Logger.getLogger(MotoController.class.getName());

    private final MotoService motoService;
    private final OrderService orderService;

    public MotoController(MotoService motoService, OrderService orderService) {
        this.motoService = motoService;
        this.orderService = orderService;
    }

    @GetMapping("/ardgetstatus")
    boolean sendStatus() {
        logger.info("moto status: " + motoService.getStatus(1));
        return motoService.getStatus(1);
    }

    @PostMapping("/ardstart")
    String startRent(@RequestBody String qrAndEmail) {
        logger.info("start rent data: " + qrAndEmail);
        return orderService.startRent(qrAndEmail);
    }

    @PostMapping("/ardend")
    String endRent(@RequestBody String id_order) throws ParseException {
        logger.info("end rent data: " + id_order);
        return orderService.endRent(id_order);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/addmoto")
    boolean addMotoToDB(@RequestBody String moto) {
        logger.info("check auth");
        logger.info("auth: " + moto);
        return motoService.addMoto(moto);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/infomoto")
    List<Motoroller> getForms() {
        logger.info("get all info about applications (called findAll())");
        return motoService.findAll();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/infolistidmoto")
    List<Integer> getListOfId() {
        logger.info("get list of appliers id (called listOfIdClients())");
        return motoService.listofidmoto();
    }
}