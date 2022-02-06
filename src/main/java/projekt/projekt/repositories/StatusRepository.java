package projekt.projekt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt.projekt.entities.Status;

import java.util.List;

public interface StatusRepository extends JpaRepository<Status, Long> {
    List<Status> findByName(String name);
}