package projekt.projekt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt.projekt.entities.Login;

import java.util.List;

public interface LoginRepository extends JpaRepository<Login, Long> {
    List<Login> findByName(String name);

    List<Login> findByToken(String token);
}