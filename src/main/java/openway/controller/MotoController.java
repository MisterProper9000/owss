package openway.controller;

import openway.model.Motoroller;
import openway.service.LesserService;
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
    private final LesserService lesserService;

    public MotoController(MotoService motoService, OrderService orderService, LesserService lesserService) {
        this.motoService = motoService;
        this.orderService = orderService;
        this.lesserService = lesserService;
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
        int id_lessor = motoService.addMoto(moto);
        return lesserService.addMotoToLesser(id_lessor);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/infomoto")
    List<Motoroller> getInfoMoto(@RequestBody String id) {
        logger.info("get all info about applications (called findAll())");
        return motoService.findLesserMoto(id);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/infolistidmoto")
    List<Integer> getListOfId() {
        logger.info("get list of appliers id (called listOfIdClients())");
        return motoService.listofidmoto();
    }
}