package openway.controller;

import openway.service.ClientService;
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
        logger.info("client reg from mob app: " + newItem);
        return clientService.addNewClient(newItem);
    }


    @PostMapping("/loginclient")
    String loginClient(@RequestBody String auth) {
        logger.info("login client from mobApp: " + auth);
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
        logger.info("moto reserve request: " + resData);
        String reserveResult = orderService.motoReserve(moto_id, resData);
        logger.info("moto reservation status(new): " + reserveResult);
        logger.info("moto reserved:" + reserveResult);
        return (reserveResult.equals("false")?"OK|":"") + reserveResult;

    }

    @PostMapping("/motoResCanc")
    String reserveCanceled(@RequestBody String resCancData) {
        logger.info("moto reserve cancel request: + " + resCancData);
        String reserveCancelResult = orderService.motoReserveCanceled(moto_id, resCancData);
        logger.info("moto reservation status(new): " + reserveCancelResult);
        return reserveCancelResult;
    }

    /**
     *
     * @param topUpData
     *
     * @return
     */

    @PostMapping("topUpCl")
    String clientTopUp(@RequestBody String topUpData){
        logger.info("client top up request: + " + topUpData);
        String clientTopUpRes = clientService.TopUp(topUpData);
        logger.info(clientTopUpRes);
        return "OK|";
    }

}