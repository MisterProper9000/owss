package openway.repository;

import openway.model.Lesser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LesserRepository extends JpaRepository<Lesser, Integer> {
    Lesser findLesserByEmail(String email);
    Lesser findLesserById(int id);
}
