package openway.service;

import openway.model.Order;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface OrderService {

    String startRent(String qrAndEmail);
    String endRent(String id_order) throws ParseException;

    String motoReserve(int moto_id, String email);
    String motoReserveCanceled(int moto_id, String data);
    String reserveTM(int id_moto);

    List<Order> listrentmobile(String email);
    List<Order> listrentForScooter(String id);

    float countAverageCost(String id);

    float countAverageCost(int id, String startStatDate, String endStatDate) throws ParseException;

    float countAverageCostOneMoto(int idmoto);

    String dataForStat(String data) throws ParseException;

    Date convertData(String data) throws ParseException;
}
