package openway.service;

import openway.model.Lesser;

public interface LesserService {
    void addNewLesser(String newLesser);
    boolean authentication(String auth);
    void setPasswordHash();

}
