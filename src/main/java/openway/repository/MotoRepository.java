package openway.repository;

import openway.model.Motoroller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotoRepository extends JpaRepository<Motoroller,Integer> {
    Motoroller findMotorollerById(int id);
}
