package openway.controller;

import openway.model.Motoroller;
import openway.model.Order;
import openway.service.LesserService;
import openway.service.MotoService;
import openway.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class MotoController {

    //?????????????????????????????????
    private int moto_id = 1;

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
    String sendStatus() {
        logger.info("moto status: rent = " + motoService.getStatusRent(moto_id) +
                " res = " + motoService.getStatusRes(moto_id));
        boolean rent = motoService.getStatusRent(moto_id);
        boolean res = motoService.getStatusRes(moto_id);

        String resTmCheck = orderService.reserveTM(moto_id);
        logger.info("check timeout in motoController");
        logger.info("" + resTmCheck);

        if(!rent && ! res){

            logger.info("false");
            return "false";
        }
        if(!rent && res){
            logger.info("falseres1");
            return "falseres1";
        }
        if(rent && res){
            logger.info("true");
            return "true";
        }
        return "dermo";
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

    @PostMapping("/ardRT")
    String resTT(@RequestBody String id_motoStr) throws ParseException {
        logger.info("reserve timeout data: " + id_motoStr);
        //int id_moto = Integer.valueOf(id_motoStr);
        return orderService.reserveTM(moto_id);
        //return orderService.reserveTm(id_moto);
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
        return motoService.findLesserMotos(id);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/infoaboutonemoto")
    Motoroller getMotoById(@RequestBody String id) {
        logger.info("info about moto"+motoService.findMotoById(id));
        return motoService.findMotoById(id);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/infolistidmoto")
    List<Integer> getListOfId() {
        logger.info("get list of appliers id (called listOfIdClients())");
        return motoService.listofidmoto();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/listScooterId")
    List<Integer> getListOfIdScooters(@RequestBody String id) {
        logger.info("get list of scooters id");
        return motoService.listofidscooters(id);
    }


    @PostMapping("/listmotomobile")
    List<Motoroller> getListMoto() {
        logger.info("get list of moto");
        return motoService.findAll();
    }

    @PostMapping("/listrentonmobile")
    List<Order> getlistrent(@RequestBody String email) {
        logger.info("getlistrent: " + email);
        return orderService.listrentmobile(email);
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/goToScooterInfo")
    String isIdScooterExist(@RequestBody String id) {
        logger.info("isIdScooterExist"+id);
        //logger.info("isIdScooterExist: "+motoService.isScooterIdExist(id));
        return motoService.isScooterIdExist(id);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/gotorentforscooter")
    List<Order> getRentInfoForScooter(@RequestBody String id) {
        logger.info("getRentInfoForScooter: "+id);
        return orderService.listrentForScooter(id);
    }
}