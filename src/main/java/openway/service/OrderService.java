package openway.service;

import openway.model.Order;

import java.text.ParseException;
import java.util.List;

public interface OrderService {
    String startRent(String qrAndEmail);
    String endRent(String id_order) throws ParseException;

    String motoReserve(int moto_id, String email);
    String motoReserveCanceled(int moto_id, String data);
    String reserveTM(int id_moto);

    List<Order> listrentmobile(String email);
    List<Order> listrentForScooter(String id);
}
