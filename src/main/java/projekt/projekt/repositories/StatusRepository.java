package projekt.projekt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt.projekt.entities.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
}