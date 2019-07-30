package openway.controller;

import openway.service.MotoService;
import openway.service.OrderService;
import org.springframework.web.bind.annotation.*;

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

//    @PostMapping("/ardstart")
//    String startRent(@RequestBody String qrAndEmail) {
//        logger.info("start rent data: "+qrAndEmail);
//        return orderService.startRent(qrAndEmail);
//    }
//
//    @PostMapping("/ardqrend")
//    String endRent(@RequestBody String id_order) {
//        logger.info("end rent data: "+id_order);
//        return orderService.endRent(id_order);
//    }
}