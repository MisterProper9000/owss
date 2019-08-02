package openway.service;

import openway.model.Motoroller;

import java.util.List;

public interface MotoService {
    boolean getStatusRent(int id);
    boolean getStatusRes(int id);

    void createQrCode(int id);
    void addMoto(String moto);
    List<Motoroller> findAll();
    List<Integer> listofidmoto();
}
