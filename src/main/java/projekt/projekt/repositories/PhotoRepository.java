package projekt.projekt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt.projekt.entities.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}