package openway.service;

import java.text.ParseException;

public interface OrderService {
    String startRent(String qrAndEmail);
    String endRent(String id_order) throws ParseException;

    String motoReserve(int moto_id, String email);
    String motoReserveCanceled(int moto_id, String data);
    String reserveTM(int id_moto);
}
