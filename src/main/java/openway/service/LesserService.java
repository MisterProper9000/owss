package openway.service;

import openway.model.Lesser;

public interface LesserService {
    void addNewLesser(String newLesser);
   Lesser authentication(String auth);
    //void setPasswordHash();

}
