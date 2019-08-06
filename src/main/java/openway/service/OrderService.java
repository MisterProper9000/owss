package openway.service;

import openway.model.Order;

import java.text.ParseException;
import java.util.List;

public interface OrderService {
    String startRent(String qrAndEmail);

    String endRent(String id_order) throws ParseException;

    String motoReserve(String data);

    String motoReserveCanceled(String data);

    String reserveTM(int id_moto);

    String checkResState(String checkResData);

    List<Order> listrentmobile(String email);
}
