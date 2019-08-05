package openway.repository;

import openway.model.Motoroller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MotoRepository extends JpaRepository<Motoroller, Integer> {
    Motoroller findMotorollerById(int id);
    List<Motoroller> findMotorollersByIdowner(int id);

    List<Motoroller> findMotorollersById(int parseInt);
}