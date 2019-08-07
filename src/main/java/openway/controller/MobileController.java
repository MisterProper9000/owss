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
        logger.info("balance request from mobApp: " + data);
        return clientService.CheckBalance(data);
    }

    @PostMapping("/payRent")
    String payRent(@RequestBody String data) {
        logger.info("pay rent request from mobApp " + data);
        return clientService.payRent(data);
    }

    @PostMapping("/motoRes")
    String reserveStart(@RequestBody String resData) {
        logger.info("moto reserve request from mobApp: " + resData);
        String reserveResult = orderService.motoReserve(resData);
        logger.info("moto reservation result: " + reserveResult);
        return reserveResult;
    }

    @PostMapping("/motoResCanc")
    String reserveCanceled(@RequestBody String resCancData) {
        logger.info("moto reserve cancel request: from mobApp: " + resCancData);
        String reserveCancelResult = orderService.motoReserveCanceled(resCancData);
        logger.info("moto reservation cancelled result: " + reserveCancelResult);
        return reserveCancelResult;
    }

    @PostMapping("/checkresstat")
    String checkResMoto(@RequestBody String checkResData)
    {
        logger.info("checkResStatus: order_id = " + checkResData);
        return orderService.checkResState(checkResData);
    }

    /**
     *
     * @param topUpData
     *
     * @return
     */
    @PostMapping("/topUpCl")
    String clientTopUp(@RequestBody String topUpData){
        logger.info("client top up request from mobApp: " + topUpData);
        String clientTopUpRes = clientService.TopUp(topUpData);
        logger.info("client top up result: " + clientTopUpRes);
        return clientTopUpRes;
    }

    @PostMapping("/topDownCl")
    String clientDown(@RequestBody String downData){
        logger.info("client down money request from mobApp: " + downData);
        String clientDownRes = clientService.DownMoney(downData);
        logger.info("client down money result: " + clientDownRes);
        return clientDownRes;
    }

}