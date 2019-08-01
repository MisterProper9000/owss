package openway.service;

import openway.model.Motoroller;

import java.util.List;

public interface MotoService {
    boolean getStatus(int id);
    void createQrCode(int id);
    boolean addMoto(String moto);
    List<Motoroller> findAll();
    List<Integer> listofidmoto();
}
