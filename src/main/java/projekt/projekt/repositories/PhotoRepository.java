package projekt.projekt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt.projekt.entities.Photo;

import javax.transaction.Transactional;
import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Transactional
    List<Photo> findByMain(boolean main);
    @Transactional
    List<Photo> findByProductId(long id);
    @Transactional
    List<Photo> findByProductIdOrderByMainDesc(long id);
}