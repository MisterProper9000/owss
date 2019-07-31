package openway.service;

import java.text.ParseException;

public interface OrderService {
    String startRent(String qrAndEmail);
    String endRent(String id_order) throws ParseException;
}
