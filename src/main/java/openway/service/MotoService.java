package openway.service;

public interface MotoService {
    boolean getStatus(int id);
    void createQrCode(int id);
    String checkQr(String qrCode);
}
