package openway.service;

import openway.model.Motoroller;

import java.util.List;

public interface MotoService {
    boolean getStatusRent(int id);
    boolean getStatusRes(int id);

    void createQrCode(int id);
    int addMoto(String moto);
    List<Motoroller> findAll();
    List<Motoroller> findLesserMotos(String id);
    List<Integer> listofidmoto();
    List<Integer> listofidscooters(String id);

    Motoroller findMotoById(String id);

    String isScooterIdExist(String id);
}
