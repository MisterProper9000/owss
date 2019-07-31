package openway.service;

public interface OrderService {
    String startRent(String qrAndEmail);
    String endRent(String id_order);
}
