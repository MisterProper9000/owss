package openway.service;

import openway.model.Lesser;

import java.util.List;

public interface LesserService {
    void addNewLesser(String newLesser);

    String authentication(String auth);

    List<Lesser> findAll();

    List<Integer> listofidlessers();

    String logout(String id);

    List<String> getNameSerName(String i);
    //void setPasswordHash();

}
