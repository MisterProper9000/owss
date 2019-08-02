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

    private int moto_id = 1;

    private final static Logger logger = Logger.getLogger(MotoController.class.getName());

    private final MotoService motoService;
    private final OrderService orderService;

    public MotoController(MotoService motoService, OrderService orderService) {
        this.motoService = motoService;
        this.orderService = orderService;
    }

    @GetMapping("/ardgetstatus")
    String sendStatus() {
        logger.info("moto status: rent = " + motoService.getStatusRent(moto_id) +
                "res = " + motoService.getStatusRes(moto_id));
        return motoService.getStatusRent(moto_id) + "res=" + motoService.getStatusRes(moto_id);
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
    void addMotoToDB(@RequestBody String moto) {
        logger.info("check auth");
        logger.info("auth: " + moto);
        motoService.addMoto(moto);
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