package openway.controller;

import openway.service.ClientService;
import openway.service.MotoService;
import openway.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;



@RestController
public class MobileController {

    private final static Logger logger = Logger.getLogger(MobileController.class.getName());

    private final ClientService clientService;
    private final OrderService orderService;
    private int moto_id = 1;


    public MobileController(ClientService clientService, OrderService orderService) {
        this.clientService = clientService;
        this.orderService = orderService;
    }

    @PostMapping("/regclient")
    String addNewClient(@RequestBody String newItem) {
        logger.info("get moto registration data" + newItem);
        return clientService.addNewClient(newItem);
    }


    @PostMapping("/loginclient")
    String loginClient(@RequestBody String auth) {
        logger.info("called loginClient()" + auth);
        return clientService.authenticationClient(auth);
    }

    @PostMapping("/balanceInquery")
    String clientBalanceRequest(@RequestBody String data) {
        logger.info("balance request: " + data);
        return clientService.CheckBalance(data);
    }

    @PostMapping("/payRent")
    String payRent(@RequestBody String data) {
        logger.info("pay rent request " + data);
        return clientService.payRent(data);
    }

    @PostMapping("/motoRes")
    String reserveStart(@RequestBody String resData) {
        logger.info("moto reserve request:" + resData);
        String reserveResult = orderService.motoReserve(moto_id, resData);
        logger.info("moto reserved:" + reserveResult);

        return reserveResult;

    }

    @PostMapping("/motoResCanc")
    String reserveCanceled(@RequestBody String resCancData) {
        logger.info("moto reserve cancel request: + " + resCancData);
        String reserveCancelResult = orderService.motoReserveCanceled(moto_id, resCancData);
        logger.info("moto reserv:" + reserveCancelResult);
        return reserveCancelResult;
    }

    @PostMapping("topUpCl")
    String clientTopUp(@RequestBody String topUpData){
        logger.info("client top up request: + " + topUpData);
        String clienttopUpRes = clientService.TopUp(topUpData);
        logger.info(clienttopUpRes);
        return "OK|";
    }

}